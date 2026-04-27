import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormsModule } from '@angular/forms';
import { WalletService } from '../../services/wallet.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { NotificationService } from '../../../../core/services/notification.service';

interface ErrorState {
  type: 'validation' | 'custom' | 'system';
  message: string;
  statusCode?: number;
}

@Component({
  selector: 'app-wallet-credit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './wallet-credit.component.html',
  styleUrl: './wallet-credit.component.css'
})
export class WalletCreditComponent {
  private svc = inject(WalletService); 
  private notify = inject(NotificationService); 
  private fb = inject(FormBuilder);
  
  userId = ''; 
  form = this.fb.group({ 
    amount: ['', Validators.required] 
  });
  
  result: any = null; 
  errorState: ErrorState | null = null;
  validationError = ''; 
  loading = false;

  submit() {
    this.validationError = '';
    this.form.markAllAsTouched();

     if (!this.userId) {
  this.validationError = 'Payment ID is required';
  return;
}



if (parseInt(this.userId) <= 0) {
  this.validationError = 'Payment ID must be greater than 0';
  return;
}
    
    if (this.form.invalid) {
      return;
    }
     
    this.loading = true; 
    this.errorState = null; 
    this.result = null;
    this.svc.credit(+this.userId, this.form.value as any).subscribe({
      next: r => { 
        this.result = r.data; 
        this.loading = false; 
        this.notify.show('Wallet credited successfully!'); 
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
