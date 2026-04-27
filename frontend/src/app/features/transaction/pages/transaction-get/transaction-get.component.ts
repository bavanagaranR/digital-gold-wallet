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
  validationError = '';
  backendError = '';
  systemError = '';
  statusCode: number | null = null;
  loading = false;

  submit() {
    this.validationError = '';
    this.backendError = '';
    this.systemError = '';
    this.statusCode = null;
    this.result = null;

    if (!this.txId) {
      this.validationError = 'Transaction ID is required';
      return;
    }
    else if(parseInt(this.txId) <= 0)
    {
      this.validationError = 'Transaction ID must be a positive number';
      return;
    }
    this.loading = true; 
    this.svc.getTransactionById(+this.txId).subscribe({ 
      next: r => { 
        this.result = r.data; 
        this.loading = false; 
      }, 
      error: e => { 
        this.statusCode = e.status;
        const msg = e.error?.message || e.error || 'Something went wrong';
        if (e.status === 400) {
          this.backendError = msg;
        } else if (e.status === 500) {
          this.systemError = 'System Exception: ' + msg;
        } else {
          this.systemError = e.status === 0 ? 'Network unreachable' : msg;
        }
        this.loading = false; 
      } 
    });
  }
}
