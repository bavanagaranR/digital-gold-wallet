export type GoldFooterErrorType = 'warning' | 'danger' | null;

export interface GoldUiErrorState {
  fieldErrors: Record<string, string[]>;
  groupedErrors: string[];
  footerMessage: string;
  footerType: GoldFooterErrorType;
  footerStatusCode: number | null;
}

const DEFAULT_SYSTEM_ERROR = 'Something went wrong. Please try again later.';

export function createEmptyGoldUiErrorState(): GoldUiErrorState {
  return {
    fieldErrors: {},
    groupedErrors: [],
    footerMessage: '',
    footerType: null,
    footerStatusCode: null
  };
}

export function mapGoldApiError(
  error: { status?: number; error?: unknown; message?: string } | null | undefined,
  knownFields: string[]
): GoldUiErrorState {
  const status = error?.status ?? 0;
  const messages = extractMessages(error?.error, error?.message);

  if (status >= 500) {
    return {
      fieldErrors: {},
      groupedErrors: [],
      footerMessage: DEFAULT_SYSTEM_ERROR,
      footerType: 'danger',
      footerStatusCode: status || 500
    };
  }

  if (status === 400 && isValidationMessageSet(messages, knownFields)) {
    const fieldErrors = mapMessagesToFields(messages, knownFields);
    return {
      fieldErrors,
      groupedErrors: messages.filter(message => !isMessageMapped(message, fieldErrors)),
      footerMessage: '',
      footerType: null,
      footerStatusCode: null
    };
  }

  if (status >= 400) {
    return {
      fieldErrors: {},
      groupedErrors: [],
      footerMessage: messages[0] ?? 'Request failed.',
      footerType: status >= 500 ? 'danger' : 'warning',
      footerStatusCode: status
    };
  }

  return {
    fieldErrors: {},
    groupedErrors: [],
    footerMessage: messages[0] ?? 'Request failed.',
    footerType: 'danger',
    footerStatusCode: null
  };
}

function extractMessages(payload: unknown, fallbackMessage?: string): string[] {
  const messages = new Set<string>();

  const tryAdd = (value: unknown) => {
    sanitizeMessage(value).forEach(message => messages.add(message));
  };

  if (payload && typeof payload === 'object') {
    const record = payload as Record<string, unknown>;
    tryAdd(record['message']);
    tryAdd(record['error']);
    tryAdd(record['errors']);
  } else {
    tryAdd(payload);
  }

  tryAdd(fallbackMessage);

  return Array.from(messages);
}

function sanitizeMessage(value: unknown): string[] {
  if (!value) {
    return [];
  }

  if (Array.isArray(value)) {
    return value.flatMap(item => sanitizeMessage(item));
  }

  if (typeof value === 'object') {
    return Object.values(value as Record<string, unknown>).flatMap(item => sanitizeMessage(item));
  }

  const normalized = String(value)
    .replace(/^An unexpected error occurred:\s*/i, '')
    .replace(/https?:\/\/\S+/gi, '')
    .replace(/\b\/api\/\S*/gi, '')
    .replace(/\bat\s+[A-Za-z0-9_.$<>]+\([^)]+\)/g, '')
    .replace(/\s+/g, ' ')
    .trim();

  if (!normalized) {
    return [];
  }

  return normalized
    .split(/\s*,\s*/)
    .map(part => part.trim())
    .filter(part => part.length > 0)
    .filter(part => !/[{}[\]"]/g.test(part))
    .filter(part => !part.toLowerCase().includes('exception'))
    .filter(part => !part.toLowerCase().includes('trace'))
    .filter(part => !/^[A-Z]:\\/i.test(part));
}

function isValidationMessageSet(messages: string[], knownFields: string[]): boolean {
  if (!messages.length) {
    return false;
  }

  return messages.every(message => hasValidationKeyword(normalize(message), knownFields));
}

function mapMessagesToFields(messages: string[], knownFields: string[]): Record<string, string[]> {
  const fieldErrors: Record<string, string[]> = {};

  for (const message of messages) {
    const matchingField = knownFields.find(field => getFieldAliases(field).some(alias => normalize(message).includes(alias)));
    if (!matchingField) {
      continue;
    }

    fieldErrors[matchingField] = [...(fieldErrors[matchingField] ?? []), message];
  }

  return fieldErrors;
}

function isMessageMapped(message: string, fieldErrors: Record<string, string[]>): boolean {
  return Object.values(fieldErrors).some(messages => messages.includes(message));
}

function normalize(value: string): string {
  return value.toLowerCase().replace(/[^a-z0-9]/g, '');
}

function hasValidationKeyword(normalizedMessage: string, knownFields: string[]): boolean {
  const referencesKnownField = knownFields.some(field =>
    getFieldAliases(field).some(alias => normalizedMessage.includes(alias))
  );

  const validationKeywords = ['required', 'greaterthan', 'invalid', 'format', 'whole', 'number'];

  return referencesKnownField && validationKeywords.some(keyword => normalizedMessage.includes(keyword));
}

function getFieldAliases(field: string): string[] {
  if (field === 'userId') {
    return ['userid'];
  }

  if (field === 'branchId') {
    return ['branchid'];
  }

  if (field === 'deliveryAddressId') {
    return ['deliveryaddressid', 'addressid'];
  }

  return [normalize(field)];
}
