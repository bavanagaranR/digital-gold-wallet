import { Component, inject } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { VendorService } from '../../services/vendor.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { NotificationService } from '../../../../core/services/notification.service';
import {
  buildResultError,
  createEmptyResultError,
  extractFieldErrors,
  getControlErrorMessage,
  ResultErrorState,
  shouldShowControlError
} from '../../vendor-error.utils';

@Component({
  selector: 'app-vendor-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, PageHeaderComponent, ResultViewerComponent, DecimalPipe],
  templateUrl: './vendor-create.component.html',
  styleUrl: './vendor-create.component.css'
})
export class VendorCreateComponent {
  private svc = inject(VendorService);
  private notify = inject(NotificationService);
  private fb = inject(FormBuilder);

  readonly form = this.fb.group({
    vendorName: ['', [Validators.required]],
    description: [''],
    contactPersonName: [''],
    contactEmail: ['', [Validators.required]],
    contactPhone: ['', [Validators.required]],
    websiteUrl: [''],
    currentGoldPrice: ['', [Validators.required]],
    totalGoldQuantity: ['']
  });

  readonly fieldLabels: Record<string, string> = {
    vendorName: 'Vendor Name',
    description: 'Description',
    contactPersonName: 'Contact Person Name',
    contactEmail: 'Email',
    contactPhone: 'Phone',
    websiteUrl: 'Website URL',
    currentGoldPrice: 'Current Gold Price'
  };

  readonly validationMessages: Record<string, Record<string, string>> = {
    vendorName: {
      required: 'Vendor name cannot be empty'
    },
    contactEmail: {
      required: 'Email is required'
    },
    contactPhone: {
      required: 'Phone number is required'
    },
    currentGoldPrice: {
      required: 'Gold price cannot be null'
    }
  };

  result: any = null;
  loading = false;
  submitted = false;
  fieldErrors: Record<string, string> = {};
  backendValidationSummary: string[] = [];
  resultError: ResultErrorState = createEmptyResultError();

  submit() {
    this.submitted = true;
    this.clearErrors();
    this.result = null;

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.svc.createVendor(this.form.value as any).subscribe({
      next: r => {
        this.result = r.data;
        this.fieldErrors = {};
        this.backendValidationSummary = [];
        this.resultError = createEmptyResultError();
        this.loading = false;
        this.notify.show('Vendor created!');
      },
      error: e => {
        const backendFieldErrors = extractFieldErrors(e);
        this.applyBackendFieldErrors(backendFieldErrors);

        if (!Object.keys(this.fieldErrors).length && !this.backendValidationSummary.length) {
          this.resultError = buildResultError(e, 'Failed to create vendor.');
        }

        this.loading = false;
      }
    });
  }

  hasControlError(fieldName: string): boolean {
    return shouldShowControlError(this.form.get(fieldName), this.submitted);
  }

  getControlError(fieldName: string): string {
    return getControlErrorMessage(this.form.get(fieldName), this.submitted, this.validationMessages[fieldName] || {});
  }

  getBackendFieldError(fieldName: string): string {
    return this.fieldErrors[fieldName] || '';
  }

  private applyBackendFieldErrors(backendFieldErrors: Record<string, string>) {
    if (!Object.keys(backendFieldErrors).length) {
      this.fieldErrors = {};
      this.backendValidationSummary = [];
      return;
    }

    const knownFields = new Set(Object.keys(this.form.controls));
    const fieldErrors: Record<string, string> = {};
    const summary: string[] = [];

    for (const [fieldName, message] of Object.entries(backendFieldErrors)) {
      if (knownFields.has(fieldName)) {
        fieldErrors[fieldName] = message;
      } else {
        const label = this.fieldLabels[fieldName] || fieldName;
        summary.push(`${label}: ${message}`);
      }
    }

    this.fieldErrors = fieldErrors;
    this.backendValidationSummary = summary;
  }

  private clearErrors() {
    this.loading = false;
    this.fieldErrors = {};
    this.backendValidationSummary = [];
    this.resultError = createEmptyResultError();
  }
}
