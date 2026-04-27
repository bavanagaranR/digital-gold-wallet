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
  selector: 'app-user-payments',
  standalone: true,
  imports: [CommonModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './user-payments.component.html',
  styleUrl: './user-payments.component.css'
})
export class UserPaymentsComponent {
  private svc = inject(PaymentService);
  userId = ''; 
  page = 0; 
  size = 10; 
  payments: any[] = []; 
  totalPages = 0; 
  totalElements = 0; 
  errorState: ErrorState | null = null;
  validationError = ''; 
  loading = false;

  load() {
    this.validationError = '';
    
    if (!this.userId) {
  this.validationError = 'Payment ID is required';
  return;
}



if (parseInt(this.userId) <= 0) {
  this.validationError = 'Payment ID must be greater than 0';
  return;
}
     
    this.loading = true; 
    this.errorState = null; 
    this.payments = [];
    this.svc.getUserPayments(+this.userId, this.page, this.size).subscribe({
      next: r => { 
        const d = r.data as any; 
        this.payments = Array.isArray(d) ? d : (d?.content ?? []); 
        this.totalPages = d?.totalPages ?? 1; 
        this.totalElements = d?.totalElements ?? this.payments.length; 
        this.loading = false; 
      },
      error: e => { 
        const status = e.status;
        const msg = e.error?.message || e.message || 'Failed';
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

  prev() { 
    if (this.page > 0) { 
      this.page--; 
      this.load(); 
    } 
  }

  next() { 
    if (this.page < this.totalPages - 1) { 
      this.page++; 
      this.load(); 
    } 
  }
}
