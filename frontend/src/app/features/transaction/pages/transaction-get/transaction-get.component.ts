import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TransactionService } from '../../services/transaction.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';

@Component({
  selector: 'app-transaction-get',
  standalone: true,
  imports: [CommonModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './transaction-get.component.html',
  styleUrl: './transaction-get.component.css'
})
export class TransactionGetComponent {
  private svc = inject(TransactionService);
  txId = ''; 
  result: any = null; 
  error = ''; 
  loading = false;

  submit() {
    if (!this.txId) {
      this.error = 'Transaction ID is required';
      return;
    }
    else if(parseInt(this.txId) <= 0)
    {
      this.error = 'Transaction ID must be a positive number';
      return;
    }
    this.loading = true; 
    this.error = ''; 
    this.result = null;
    this.svc.getTransactionById(+this.txId).subscribe({ 
      next: r => { 
        this.result = r.data; 
        this.loading = false; 
      }, 
      error: e => { 
        this.error = e.error?.message || e.message || 'Not found'; 
        this.loading = false; 
      } 
    });
  }
}
