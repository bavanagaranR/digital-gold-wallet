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
  selector: 'app-branch-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './branch-create.component.html',
  styleUrl: './branch-create.component.css'
})
export class BranchCreateComponent {
  private svc = inject(VendorService);
  private fb = inject(FormBuilder);
  private notify = inject(NotificationService);

  vendorId = '';
  readonly form = this.fb.group({
    addressId: [''],
    quantity: ['']
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
    const addressId = this.form.value.addressId;
  const quantity = this.form.value.quantity;

  // ✅ Address ID validation
  if (!addressId) {
    this.fieldErrors['addressId'] = 'Address ID is required.';
    return;
  }

  if (Number(addressId) <= 0) {
    this.fieldErrors['addressId'] = 'Address ID must be greater than 0.';
    return;
  }

  // ✅ Quantity validation (optional but good)
  if (quantity !== null && quantity !== '' && Number(quantity) < 0) {
    this.fieldErrors['quantity'] = 'Quantity cannot be negative.';
    return;
  }

    this.loading = true;
    this.svc.createBranch(+this.vendorId, this.form.value as any).subscribe({
      next: r => {
        this.result = r.data;
        this.fieldErrors = {};
        this.backendValidationSummary = [];
        this.resultError = createEmptyResultError();
        this.loading = false;
        this.notify.show('Branch created!');
      },
      error: e => {
        const backendFieldErrors = extractFieldErrors(e);
        this.applyBackendFieldErrors(backendFieldErrors);

        if (!Object.keys(this.fieldErrors).length && !this.backendValidationSummary.length) {
          this.resultError = buildResultError(e, 'Failed to create branch.');
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
