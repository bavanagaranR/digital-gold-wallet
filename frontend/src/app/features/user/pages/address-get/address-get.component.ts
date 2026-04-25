import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';

@Component({
  selector: 'app-address-get',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './address-get.component.html',
  styleUrl: './address-get.component.css'
})
export class AddressGetComponent {
  private svc = inject(UserService);
  addressId = ''; 
  result: any = null; 
  error = ''; 
  loading = false;

  submit() {
    if (!this.addressId) return;
    this.loading = true; 
    this.error = ''; 
    this.result = null;
    this.svc.getAddressById(+this.addressId).subscribe({
      next: r => { 
        this.result = r.data; 
        this.loading = false; 
      },
      error: e => { 
        this.error = e.error?.message || e.message || 'Not found'; 
        this.loading = false; 
      }
    });
  }
}
