import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TransactionService } from '../../services/transaction.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';

@Component({
  selector: 'app-branch-transactions',
  standalone: true,
  imports: [CommonModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './branch-transactions.component.html',
  styleUrl: './branch-transactions.component.css'
})
export class BranchTransactionsComponent {
  private svc = inject(TransactionService);
  branchId = ''; 
  page = 0; 
  size = 10; 
  txns: any[] = []; 
  totalPages = 0; 
  totalElements = 0; 
  error = ''; 
  loading = false;

  load() {
    if (!this.branchId) {
      this.error = 'Branch ID is required';
      return;
    }
    this.loading = true; 
    this.error = ''; 
    this.txns = [];
    this.svc.getBranchTransactions(+this.branchId, this.page, this.size).subscribe({
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
