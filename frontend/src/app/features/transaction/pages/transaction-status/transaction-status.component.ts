import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TransactionService } from '../../services/transaction.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';

@Component({
  selector: 'app-transaction-status',
  standalone: true,
  imports: [CommonModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './transaction-status.component.html',
  styleUrl: './transaction-status.component.css'
})
export class TransactionByStatusComponent {
  private svc = inject(TransactionService);
  status = ''; 
  page = 0; 
  size = 10; 
  txns: any[] = []; 
  totalPages = 0; 
  totalElements = 0; 
  error = ''; 
  loading = false;

  load() {
    if (!this.status) {
      this.error = 'Status is required';
      return;
    }
    this.loading = true; 
    this.error = ''; 
    this.txns = [];
    this.svc.getTransactionsByStatus(this.status, this.page, this.size).subscribe({
      next: r => { 
        const d = r.data as any; 
        this.txns = d?.content ?? (Array.isArray(d) ? d : []); 
        this.totalPages = d?.totalPages ?? 1; 
        this.totalElements = d?.totalElements ?? this.txns.length; 
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
