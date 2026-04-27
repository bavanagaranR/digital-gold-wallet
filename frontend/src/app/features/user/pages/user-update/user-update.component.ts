import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { NotificationService } from '../../../../core/services/notification.service';

@Component({
  selector: 'app-user-update',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './user-update.component.html',
  styleUrl: './user-update.component.css'
})
export class UserUpdateComponent {
  private svc = inject(UserService);
  private fb = inject(FormBuilder);
  private notify = inject(NotificationService);
  
  userId = '';
  form = this.fb.group({ 
    name: [''], 
    email: [''], 
    addressId: [''] 
  });
  
  result: any = null; 
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
    this.result = null;
    const body = Object.fromEntries(Object.entries(this.form.value).filter(([, v]) => v));
    this.svc.updateUser(+this.userId, body).subscribe({
      next: r => { 
        this.result = r.data; 
        this.loading = false; 
        this.notify.show('User updated!'); 
      },
      error: e => { 
        this.error = e.error?.message || e.message || 'Failed'; 
        this.loading = false; 
      }
    });
  }
}
