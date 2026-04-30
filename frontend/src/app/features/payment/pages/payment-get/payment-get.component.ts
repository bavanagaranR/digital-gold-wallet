import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PaymentService } from '../../services/payment.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';

interface ErrorState {
  type: 'validation' | 'custom' | 'system';
  message: string;
  statusCode?: number;
}

@Component({
  selector: 'app-payment-get',
  standalone: true,
  imports: [CommonModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './payment-get.component.html',
  styleUrl: './payment-get.component.css'
})
export class PaymentGetComponent {
  private svc = inject(PaymentService);
  paymentId = ''; 
  result: any = null; 
  errorState: ErrorState | null = null;
  validationError = ''; 
  loading = false;
 validatePaymentId() {
   if (!this.paymentId) {
    this.validationError = 'Payment ID is required';
  } else if (parseInt(this.paymentId) <= 0) {
    this.validationError = 'Payment ID must be greater than 0';
  } else {
    this.validationError = '';
  };
    
  }
  submit() {
    this.validationError = '';
   if (!this.paymentId) {
  this.validationError = 'Payment ID is required';
  return;
}



if (parseInt(this.paymentId) <= 0) {
  this.validationError = 'Payment ID must be greater than 0';
  return;
}
    
    this.loading = true; 
    this.errorState = null; 
    this.result = null;
    this.svc.getPaymentById(+this.paymentId).subscribe({ 
      next: r => { 
        this.result = r.data; 
        this.loading = false; 
      }, 
      error: e => { 
        const status = e.status;
        const msg = e.error?.message || e.message || 'Not found';
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
