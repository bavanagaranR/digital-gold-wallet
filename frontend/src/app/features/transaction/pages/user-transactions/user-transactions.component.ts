import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TransactionService } from '../../services/transaction.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';

@Component({
  selector: 'app-user-transactions',
  standalone: true,
  imports: [CommonModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './user-transactions.component.html',
  styleUrl: './user-transactions.component.css'
})
export class UserTransactionsComponent {
  private svc = inject(TransactionService);
  userId = ''; 
  page = 0; 
  size = 10; 
  txns: any[] = []; 
  totalPages = 0; 
  totalElements = 0; 
  error = ''; 
  loading = false;

  load() {
    if (!this.userId) {
      this.error = 'User ID is required';
      return;
    }
    else if(parseInt(this.userId) <= 0)
    {
      this.error = 'User ID must be a greater number';
      return;
    }
    this.loading = true; 
    this.error = ''; 
    this.txns = [];
    this.svc.getUserTransactions(+this.userId, this.page, this.size).subscribe({
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
