import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { NotificationService } from '../../../../core/services/notification.service';

@Component({
  selector: 'app-address-update',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './address-update.component.html',
  styleUrl: './address-update.component.css'
})
export class AddressUpdateComponent {
  private svc = inject(UserService);
  private fb = inject(FormBuilder);
  private notify = inject(NotificationService);
  
  addressId = '';
  form = this.fb.group({ 
    street: [''], 
    city: [''], 
    state: [''], 
    postalCode: ['', [Validators.pattern(/^[a-zA-Z0-9\- ]{4,20}$/)]], 
    country: [''] 
  });
  
  result: any = null; 
  error = ''; 
  loading = false;

  submit() {
    if (!this.addressId) {
      this.error = 'Enter Address ID';
      return;
    }
    this.loading = true; 
    this.error = ''; 
    this.result = null;
    const body = Object.fromEntries(Object.entries(this.form.value).filter(([, v]) => v));
    this.svc.updateAddress(+this.addressId, body).subscribe({
      next: r => { 
        this.result = r.data; 
        this.loading = false; 
        this.notify.show('Address updated!'); 
      },
      error: e => { 
        this.error = e.error?.message || e.message || 'Failed'; 
        this.loading = false; 
      }
    });
  }
}
