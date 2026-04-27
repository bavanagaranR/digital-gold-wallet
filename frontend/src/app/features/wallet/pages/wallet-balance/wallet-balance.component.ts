import { Component, inject } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { WalletService } from '../../services/wallet.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';

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
  error = ''; 
  loading = false;

  submit() {
     if(parseInt(this.userId) <= 0)
    {
      this.error = 'User ID must be a greater than 0';
      return;
    }
   else if (!this.userId) {
      this.error = 'User ID is required';
      return;
    } 
    this.loading = true; 
    this.error = ''; 
    this.balance = null;
    this.svc.getBalance(+this.userId).subscribe({
      next: r => { 
        const d = r.data as any; 
        this.balance = d?.balance ?? d; 
        this.loading = false; 
      },
      error: e => { 
        this.error = e.error?.message || e.message || 'Failed'; 
        this.loading = false; 
      }
    });
  }
}
