import { AbstractControl, FormGroup } from '@angular/forms';

export type UserFeedbackType = 'none' | 'validation' | 'custom' | 'system';

export interface UserFeedbackState {
  type: UserFeedbackType;
  status: number | null;
  message: string;
  fieldErrors: Record<string, string[]>;
  groupedMessages: string[];
}

const GENERIC_SYSTEM_MESSAGE = 'Something went wrong. Please try again later.';

const FIELD_MESSAGE_LOOKUP: Record<string, RegExp[]> = {
  name: [
    /^name is required$/i,
    /^name cannot be empty$/i,
    /^name must contain only letters$/i,
    /^name must be between 2 and 100 characters$/i,
    /^name must be at least 2 characters(?: long)?$/i,
    /^name must be 100 characters or fewer$/i,
  ],
  email: [
    /^email is required$/i,
    /^email cannot be empty$/i,
    /^invalid email format$/i,
    /^email must be 100 characters or fewer$/i,
  ],
  addressId: [
    /^address id is required$/i,
    /^address id must be greater than 0$/i,
  ],
  userId: [
    /^user id is required$/i,
    /^user id must be greater than 0$/i,
  ],
  street: [
    /^street is required$/i,
    /^street cannot be empty$/i,
    /^street must be less than 255 characters$/i,
  ],
  city: [
    /^city is required$/i,
    /^city cannot be empty$/i,
    /^city must be less than 100 characters$/i,
  ],
  state: [
    /^state is required$/i,
    /^state cannot be empty$/i,
    /^state must be less than 100 characters$/i,
  ],
  postalCode: [
    /^postal code is required$/i,
    /^invalid postal code$/i,
    /^postal code must be a 6-digit$/i,
  ],
  country: [
    /^country is required$/i,
    /^country cannot be empty$/i,
    /^country must be less than 100 characters$/i,
  ],
  page: [
    /^page is required$/i,
    /^page must be 0 or greater$/i,
    /^page must be a whole number$/i,
  ],
  size: [
    /^size is required$/i,
    /^size must be at least 1$/i,
    /^size must be 100 or less$/i,
    /^size must be between 1 and 100$/i,
    /^size must be a whole number$/i,
  ],
};

const CONTROL_MESSAGE_LOOKUP: Record<string, Record<string, string>> = {
  name: {
    required: 'Name is required',
    minlength: 'Name must be at least 2 characters long',
    maxlength: 'Name must be 100 characters or fewer',
    pattern: 'Name must contain only letters',
    backend: 'Name is invalid',
  },
  email: {
    required: 'Email is required',
    email: 'Enter a valid email address',
    pattern: 'Invalid email format',
    maxlength: 'Email must be 100 characters or fewer',
    backend: 'Email is invalid',
  },
  addressId: {
    required: 'Address ID is required',
    min: 'Address ID must be greater than 0',
    pattern: 'Address ID must be a whole number',
    backend: 'Address ID must be greater than 0',
  },
  userId: {
    required: 'User ID is required',
    min: 'User ID must be greater than 0',
    pattern: 'User ID must be a whole number',
    backend: 'User ID must be greater than 0',
  },
  street: {
    required: 'Street is required',
    maxlength: 'Street must be 255 characters or fewer',
    backend: 'Street is invalid',
  },
  city: {
    required: 'City is required',
    maxlength: 'City must be 100 characters or fewer',
    backend: 'City is invalid',
  },
  state: {
    required: 'State is required',
    maxlength: 'State must be 100 characters or fewer',
    backend: 'State is invalid',
  },
  postalCode: {
    required: 'Postal Code is required',
    pattern: 'Postal Code must be a 6-digit',
    maxlength: 'Postal Code must be 6 digits',
    backend: 'Invalid postal code',
  },
  country: {
    required: 'Country is required',
    maxlength: 'Country must be 100 characters or fewer',
    backend: 'Country is invalid',
  },
  page: {
    required: 'Page is required',
    min: 'Page must be 0 or greater',
    pattern: 'Page must be a whole number',
    backend: 'Page must be 0 or greater',
  },
  size: {
    required: 'Size is required',
    min: 'Size must be at least 1',
    max: 'Size must be 100 or less',
    pattern: 'Size must be a whole number',
    backend: 'Size must be between 1 and 100',
  },
};

export function createEmptyUserFeedbackState(): UserFeedbackState {
  return {
    type: 'none',
    status: null,
    message: '',
    fieldErrors: {},
    groupedMessages: [],
  };
}

export function clearBackendErrors(form: FormGroup): void {
  Object.values(form.controls).forEach((control) => {
    const errors = control.errors;
    if (!errors || !('backend' in errors)) {
      return;
    }

    const { backend, backendMessages, ...rest } = errors as Record<string, any>;
    const nextErrors = Object.keys(rest).length ? rest : null;
    control.setErrors(nextErrors);
  });
}

export function applyBackendFieldErrors(form: FormGroup, fieldErrors: Record<string, string[]>): void {
  Object.entries(fieldErrors).forEach(([field, messages]) => {
    const control = form.get(field);
    if (!control) {
      return;
    }

    const currentErrors = control.errors ?? {};
    control.setErrors({
      ...currentErrors,
      backend: messages[0],
      backendMessages: messages,
    });
  });
}

export function markAllTouched(form: FormGroup): void {
  Object.values(form.controls).forEach((control) => control.markAsTouched());
}

export function shouldShowControlError(control: AbstractControl | null, submitted: boolean): boolean {
  return !!control && control.invalid && (control.touched || submitted);
}

export function getControlErrorMessage(control: AbstractControl | null, fieldName: string): string {
  if (!control || !control.errors) {
    return '';
  }

  const fieldMessages = CONTROL_MESSAGE_LOOKUP[fieldName] ?? {};
  if (control.errors['backend']) {
    return control.errors['backend'];
  }

  for (const key of Object.keys(fieldMessages)) {
    if (control.errors[key]) {
      return fieldMessages[key];
    }
  }

  if (control.errors['required']) {
    return fieldMessages['required'] ?? 'This field is required';
  }

  return fieldMessages['backend'] ?? 'Invalid value';
}

export function parseUserFeedback(error: any): UserFeedbackState {
  const status = Number(error?.status ?? error?.error?.status ?? 0);
  const rawMessage = extractErrorMessage(error);

  if (!status || status >= 500) {
    return {
      ...createEmptyUserFeedbackState(),
      type: 'system',
      status: status || null,
      message: GENERIC_SYSTEM_MESSAGE,
    };
  }

  const validation = mapValidationMessages(rawMessage);
  if (Object.keys(validation.fieldErrors).length > 0) {
    return {
      type: 'validation',
      status,
      message: 'Please fix the highlighted fields.',
      fieldErrors: validation.fieldErrors,
      groupedMessages: validation.groupedMessages,
    };
  }

  return {
    type: 'custom',
    status,
    message: rawMessage || 'Request failed.',
    fieldErrors: {},
    groupedMessages: [],
  };
}

export function extractErrorMessage(error: any): string {
  if (!error) {
    return '';
  }

  if (typeof error === 'string') {
    return error;
  }

  return (
    error?.error?.message ??
    error?.error?.error?.message ??
    error?.message ??
    error?.error?.messageText ??
    ''
  );
}

export abstract class UserFormSupport {
  form!: FormGroup;
  submitted = false;
  feedback: UserFeedbackState = createEmptyUserFeedbackState();

  protected startSubmit(): void {
    this.submitted = true;
    markAllTouched(this.form);
    clearBackendErrors(this.form);
    this.feedback = createEmptyUserFeedbackState();
  }

  protected resetFeedback(): void {
    clearBackendErrors(this.form);
    this.feedback = createEmptyUserFeedbackState();
  }

  protected applyServerError(error: any): void {
    const nextFeedback = parseUserFeedback(error);
    this.feedback = nextFeedback;
    clearBackendErrors(this.form);
    applyBackendFieldErrors(this.form, nextFeedback.fieldErrors);
  }
}

function mapValidationMessages(rawMessage: string): { fieldErrors: Record<string, string[]>; groupedMessages: string[] } {
  const fieldErrors: Record<string, string[]> = {};
  const groupedMessages: string[] = [];
  const messages = rawMessage
    .split(',')
    .map((part) => part.trim())
    .filter(Boolean);

  for (const message of messages) {
    const field = inferFieldFromMessage(message);
    if (!field) {
      groupedMessages.push(message);
      continue;
    }

    fieldErrors[field] = fieldErrors[field] ?? [];
    fieldErrors[field].push(message);
  }

  return { fieldErrors, groupedMessages };
}

function inferFieldFromMessage(message: string): string | null {
  for (const [field, patterns] of Object.entries(FIELD_MESSAGE_LOOKUP)) {
    if (patterns.some((pattern) => pattern.test(message))) {
      return field;
    }
  }

  return null;
}
