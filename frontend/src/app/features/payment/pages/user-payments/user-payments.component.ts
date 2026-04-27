import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PaymentService } from '../../services/payment.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';

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
  error = ''; 
  loading = false;

  load() {
    if(parseInt(this.userId) <= 0)
    {
      this.error = 'user ID must be a greater than 0';
      return;
    }
   else if (!this.userId) {
      this.error = 'User ID is required';
      return;
    } 
     
    this.loading = true; 
    this.error = ''; 
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
        this.error = e.error?.message || e.message || 'Failed'; 
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
