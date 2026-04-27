import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AbstractControl, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { GoldService } from '../../services/gold.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { NotificationService } from '../../../../core/services/notification.service';
import { createEmptyGoldUiErrorState, GoldUiErrorState, mapGoldApiError } from '../../utils/gold-error.utils';

@Component({
  selector: 'app-gold-sell',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './gold-sell.component.html',
  styleUrl: './gold-sell.component.css'
})
export class GoldSellComponent {
  private svc = inject(GoldService);
  private notify = inject(NotificationService);
  private fb = inject(FormBuilder);

  readonly knownFields = ['userId', 'branchId', 'quantity'];
  submitted = false;

  form = this.fb.group({
    userId: ['', [Validators.required, Validators.min(1), Validators.pattern(/^\d+$/)]],
    branchId: ['', [Validators.required, Validators.min(1), Validators.pattern(/^\d+$/)]],
    quantity: ['', [Validators.required, Validators.min(0.01), Validators.pattern(/^\d{1,16}(\.\d{1,2})?$/)]]
  });

  result: any = null;
  error = '';
  apiErrors: GoldUiErrorState = createEmptyGoldUiErrorState();
  loading = false;

  submit() {
    this.submitted = true;
    this.clearErrors();
    this.result = null;

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.result = null;
    this.svc.sellGold(this.form.value as any).subscribe({
      next: r => {
        this.result = r.data || r;
        this.loading = false;
        this.notify.show('Gold sold successfully!');
      },
      error: (e: HttpErrorResponse) => {
        this.apiErrors = mapGoldApiError(e, this.knownFields);
        this.error = this.apiErrors.footerMessage;
        this.loading = false;
      }
    });
  }

  control(name: 'userId' | 'branchId' | 'quantity'): AbstractControl | null {
    return this.form.get(name);
  }

  showFieldValidation(name: 'userId' | 'branchId' | 'quantity'): boolean {
    const control = this.control(name);
    return !!control && control.invalid && (control.touched || this.submitted);
  }

  validationMessage(name: 'userId' | 'branchId' | 'quantity'): string {
    const control = this.control(name);
    if (!control?.errors) {
      return '';
    }

    if (control.errors['required']) {
      return this.labelFor(name) + ' is required';
    }

    if (control.errors['min']) {
      return this.labelFor(name) + ' must be greater than 0';
    }

    if (control.errors['pattern']) {
      return name === 'quantity' ? 'Invalid quantity format' : this.labelFor(name) + ' must be a whole number';
    }

    return 'Invalid value';
  }

  backendFieldErrors(name: 'userId' | 'branchId' | 'quantity'): string[] {
    return this.apiErrors.fieldErrors[name] ?? [];
  }

  private clearErrors(): void {
    this.error = '';
    this.apiErrors = createEmptyGoldUiErrorState();
  }

  private labelFor(name: 'userId' | 'branchId' | 'quantity'): string {
    if (name === 'quantity') {
      return 'Quantity';
    }

    return name === 'userId' ? 'User ID' : 'Branch ID';
  }
}
