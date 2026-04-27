import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { NotificationService } from '../../../../core/services/notification.service';

@Component({
  selector: 'app-user-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './user-create.component.html',
  styleUrl: './user-create.component.css'
})
export class UserCreateComponent {
  private svc = inject(UserService);
  private notify = inject(NotificationService);
  private fb = inject(FormBuilder);
  
  form = this.fb.group({ 
    name: ['', Validators.required], 
    email: ['', [Validators.required, Validators.email]], 
    addressId: [''] 
  });
  
  result: any = null; 
  error = ''; 
  loading = false;

  submit() {
     if (parseInt(this.addressId)<=0 ) {
      this.error = 'Address ID must be a greater than 0';
      return;
    
    }
   else if (!this.addressId) {
      this.error = 'Address Id  is required';
      return;
    }
    this.loading = true; 
    this.error = ''; 
    this.result = null;
    this.svc.createUser(this.form.value as any).subscribe({
      next: r => { 
        this.result = r.data; 
        this.loading = false; 
        this.notify.show('User created successfully!'); 
      },
      error: e => { 
        this.error = e.error?.message || e.message || 'Request failed'; 
        this.loading = false; 
        this.notify.show('Error: ' + this.error, 'error'); 
      }
    });
  }
}
