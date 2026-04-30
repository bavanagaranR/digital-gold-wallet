import { AbstractControl } from '@angular/forms';

export type ResultErrorType = 'warning' | 'danger' | null;

export interface ResultErrorState {
  type: ResultErrorType;
  title: string;
  message: string;
  statusCode: number | null;
}

const URL_PATTERN = /^(https?:\/\/)([\w-]+\.)+[\w-]{2,}(\/[^\s]*)?$/i;
const PHONE_PATTERN = /^(\+\d{1,3}\s?)?[0-9]{10}$/;
const TWO_DECIMAL_PATTERN = /^\d{1,16}(\.\d{1,2})?$/;

export const vendorValidationPatterns = {
  url: URL_PATTERN,
  phone: PHONE_PATTERN,
  decimalAmount: TWO_DECIMAL_PATTERN
};

export function createEmptyResultError(): ResultErrorState {
  return {
    type: null,
    title: '',
    message: '',
    statusCode: null
  };
}

export function extractFieldErrors(errorResponse: any): Record<string, string> {
  const response = errorResponse?.error;
  const candidates = [
    response?.fieldErrors,
    response?.errors,
    response?.validationErrors,
    response?.data
  ];

  for (const candidate of candidates) {
    const mappedErrors = mapFieldErrors(candidate);
    if (Object.keys(mappedErrors).length) {
      return mappedErrors;
    }
  }

  return {};
}

export function buildResultError(errorResponse: any, fallbackMessage = 'Request failed.'): ResultErrorState {
  const statusCode = typeof errorResponse?.status === 'number' ? errorResponse.status : null;
  const backendMessage = sanitizeMessage(errorResponse?.error?.message || errorResponse?.message);

  if (statusCode !== null && statusCode >= 500) {
    return {
      type: 'danger',
      title: 'System Error',
      message: 'Something went wrong. Please try again later.',
      statusCode
    };
  }

  return {
    type: 'warning',
    title: 'Request Error',
    message: backendMessage || fallbackMessage,
    statusCode
  };
}

export function shouldShowControlError(control: AbstractControl | null, submitted: boolean): boolean {
  return !!control && control.invalid && (control.touched || submitted);
}

export function getControlErrorMessage(
  control: AbstractControl | null,
  submitted: boolean,
  messages: Partial<Record<string, string>>
): string {
  if (!shouldShowControlError(control, submitted) || !control?.errors) {
    return '';
  }

  const errorKeys = Object.keys(control.errors);
  for (const key of errorKeys) {
    const message = messages[key];
    if (message) {
      return message;
    }
  }

  return 'Invalid value.';
}

function mapFieldErrors(source: any): Record<string, string> {
  if (!source) {
    return {};
  }

  if (Array.isArray(source)) {
    return source.reduce((acc, item) => {
      const fieldName = item?.field || item?.fieldName || item?.path;
      const message = sanitizeMessage(item?.message || item?.defaultMessage);

      if (fieldName && message && !acc[fieldName]) {
        acc[fieldName] = message;
      }

      return acc;
    }, {} as Record<string, string>);
  }

  if (typeof source === 'object') {
    return Object.entries(source).reduce((acc, [fieldName, message]) => {
      const sanitized = sanitizeMessage(message);
      if (sanitized) {
        acc[fieldName] = sanitized;
      }

      return acc;
    }, {} as Record<string, string>);
  }

  return {};
}

function sanitizeMessage(message: unknown): string {
  if (typeof message !== 'string') {
    return '';
  }

  const trimmed = message.trim();
  if (!trimmed) {
    return '';
  }

  if (trimmed.startsWith('{') || trimmed.startsWith('[')) {
    return '';
  }

  if (/exception|stack trace|at\s+\S+\(|\/api\/|https?:\/\//i.test(trimmed)) {
    return '';
  }

  return trimmed;
}
