import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { NotificationService } from '../../../../core/services/notification.service';

@Component({
  selector: 'app-address-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './address-create.component.html',
  styleUrl: './address-create.component.css'
})
export class AddressCreateComponent {
  private svc = inject(UserService);
  private notify = inject(NotificationService);
  private fb = inject(FormBuilder);
  
  form = this.fb.group({ 
    street: ['', Validators.required], 
    city: ['', Validators.required], 
    state: ['', Validators.required], 
    postalCode: ['', [Validators.pattern(/^[a-zA-Z0-9\- ]{4,20}$/)]], 
    country: ['India', Validators.required] 
  });
  
  result: any = null; 
  error = ''; 
  loading = false;

  submit() {
    this.loading = true; 
    this.error = ''; 
    this.result = null;
    this.svc.createAddress(this.form.value as any).subscribe({
      next: r => { 
        this.result = r.data; 
        this.loading = false; 
        this.notify.show('Address created!'); 
      },
      error: e => { 
        this.error = e.error?.message || e.message || 'Failed'; 
        this.loading = false; 
      }
    });
  }
}
