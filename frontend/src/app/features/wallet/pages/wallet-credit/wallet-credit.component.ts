import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormsModule } from '@angular/forms';
import { WalletService } from '../../services/wallet.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { NotificationService } from '../../../../core/services/notification.service';

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
    this.result = null;
    this.svc.credit(+this.userId, this.form.value as any).subscribe({
      next: r => { 
        this.result = r.data; 
        this.loading = false; 
        this.notify.show('Wallet credited successfully!'); 
      },
      error: e => { 
        this.error = e.error?.message || e.message || 'Failed'; 
        this.loading = false; 
      }
    });
  }
}
