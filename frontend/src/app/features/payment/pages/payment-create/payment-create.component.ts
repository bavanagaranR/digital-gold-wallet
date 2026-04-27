import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { PaymentService } from '../../services/payment.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { NotificationService } from '../../../../core/services/notification.service';

interface ErrorState {
  type: 'validation' | 'custom' | 'system';
  message: string;
  statusCode?: number;
}

@Component({
  selector: 'app-payment-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './payment-create.component.html',
  styleUrl: './payment-create.component.css'
})
export class PaymentCreateComponent {
  private svc = inject(PaymentService); 
  private notify = inject(NotificationService); 
  private fb = inject(FormBuilder);

  form = this.fb.group({ 
    userId: ['', Validators.required], 
    amount: ['', Validators.required], 
    paymentMethod: ['', Validators.required], 
    transactionType: ['', Validators.required] 
  });

  result: any = null; 
  errorState: ErrorState | null = null; 
  loading = false;

  submit() {
    this.form.markAllAsTouched();
    if (this.form.invalid) {
      return;
    }
    this.loading = true; 
    this.errorState = null; 
    this.result = null;
    this.svc.initiatePayment(this.form.value as any).subscribe({
      next: r => { 
        this.result = r.data; 
        this.loading = false; 
        this.notify.show('Payment initiated!'); 
      },
      error: e => { 
        const status = e.status;
        const msg = e.error?.message || e.message || 'Payment failed';
        if (status === 400) {
            this.errorState = { type: 'validation', message: msg, statusCode: status };
        } else if (status > 400 && status < 500) {
            this.errorState = { type: 'custom', message: msg, statusCode: status };
        } else {
            this.errorState = { type: 'system', message: 'An unexpected system error occurred. Please try again later.' };
        }
        this.loading = false; 
      }
    });
  }
}
