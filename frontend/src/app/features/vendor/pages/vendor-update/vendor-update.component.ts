import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { VendorService } from '../../services/vendor.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { NotificationService } from '../../../../core/services/notification.service';
import {
  buildResultError,
  createEmptyResultError,
  extractFieldErrors,
  ResultErrorState,
} from '../../vendor-error.utils';

@Component({
  selector: 'app-vendor-update',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './vendor-update.component.html',
  styleUrl: './vendor-update.component.css'
})
export class VendorUpdateComponent {
  private svc = inject(VendorService);
  private fb = inject(FormBuilder);
  private notify = inject(NotificationService);

  vendorId = '';
  readonly form = this.fb.group({
    vendorName: [''],
    description: [''],
    contactPersonName: [''],
    contactEmail: [''],
    contactPhone: [''],
    websiteUrl: [''],
    currentGoldPrice: [''],
    totalGoldQuantity: ['']
  });

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

    if (!this.vendorId) {
      return;
    }

    if (Number(this.vendorId) <= 0) {
      return;
    }

    const body = Object.fromEntries(
      Object.entries(this.form.value).filter(([, value]) => value !== null && value !== undefined && value !== '')
    );

    if (!Object.keys(body).length) {
      return;
    }

    this.loading = true;
    this.svc.updateVendor(+this.vendorId, body).subscribe({
      next: r => {
        this.result = r.data;
        this.fieldErrors = {};
        this.backendValidationSummary = [];
        this.resultError = createEmptyResultError();
        this.loading = false;
        this.notify.show('Vendor updated!');
      },
      error: e => {
        const backendFieldErrors = extractFieldErrors(e);
        this.applyBackendFieldErrors(backendFieldErrors);

        if (!Object.keys(this.fieldErrors).length && !this.backendValidationSummary.length) {
          this.resultError = buildResultError(e, 'Failed to update vendor.');
        }

        this.loading = false;
      }
    });
  }

  getBackendFieldError(fieldName: string): string {
    return this.fieldErrors[fieldName] || '';
  }

  getVendorIdError(): string {
    if (!this.submitted) {
      return '';
    }

    if (!this.vendorId) {
      return 'Vendor ID is required.';
    }

    if (Number(this.vendorId) <= 0) {
      return 'Vendor ID must be greater than 0.';
    }

    return '';
  }

  private applyBackendFieldErrors(backendFieldErrors: Record<string, string>) {
    const knownFields = new Set(Object.keys(this.form.controls));
    const fieldErrors: Record<string, string> = {};
    const summary: string[] = [];

    for (const [fieldName, message] of Object.entries(backendFieldErrors)) {
      if (knownFields.has(fieldName)) {
        fieldErrors[fieldName] = message;
      } else {
        summary.push(`${fieldName}: ${message}`);
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
