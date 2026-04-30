import { Component, inject } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { WalletService } from '../../services/wallet.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';

interface ErrorState {
  type: 'validation' | 'custom' | 'system';
  message: string;
  statusCode?: number;
}

@Component({
  selector: 'app-wallet-balance',
  standalone: true,
  imports: [CommonModule, FormsModule, DecimalPipe, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './wallet-balance.component.html',
  styleUrl: './wallet-balance.component.css'
})
export class WalletBalanceComponent {
  private svc = inject(WalletService);
  userId = ''; 
  balance: number | null = null; 
  errorState: ErrorState | null = null;
  validationError = ''; 
  loading = false;
  validateUserId() {
   if (!this.userId) {
    this.validationError = 'User ID is required';
  } else if (parseInt(this.userId) <= 0) {
    this.validationError = 'User ID must be greater than 0';
  } else {
    this.validationError = '';
  };
    
  }

  submit() {
    
      if (!this.userId) {
    this.validationError = 'User ID is required';
    return
  }
  else if (parseInt(this.userId) <= 0) {
    this.validationError = 'User ID must be greater than 0';
    return
  }
    this.loading = true; 
    this.errorState = null; 
    this.balance = null;
    this.svc.getBalance(+this.userId).subscribe({
      next: r => { 
        const d = r.data as any; 
        this.balance = d?.balance ?? d; 
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
}
