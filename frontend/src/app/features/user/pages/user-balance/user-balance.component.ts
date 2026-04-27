import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';

@Component({
  selector: 'app-user-balance',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './user-balance.component.html',
  styleUrl: './user-balance.component.css'
})
export class UserBalanceComponent {
  private svc = inject(UserService);
  userId = ''; 
  balance: number | null = null; 
  error = ''; 
  loading = false;

  submit() {
    if (parseInt(this.userId)<=0 ) {
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
    this.svc.getUserBalance(+this.userId).subscribe({
      next: r => { 
        this.balance = (r.data as any)?.balance ?? r.data; 
        this.loading = false; 
      },
      error: e => { 
        this.error = e.error?.message || e.message || 'Failed'; 
        this.loading = false; 
      }
    });
  }
}
