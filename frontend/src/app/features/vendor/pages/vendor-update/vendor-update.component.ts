import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormsModule } from '@angular/forms';
import { VendorService } from '../../services/vendor.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { NotificationService } from '../../../../core/services/notification.service';

@Component({
  selector: 'app-vendor-update',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './vendor-update.component.html',
  styleUrl: './vendor-update.component.css'
})
export class VendorUpdateComponent {
  private svc = inject(VendorService); 
  private fb = inject(FormBuilder); 
  private notify = inject(NotificationService);
  
  vendorId = ''; 
  form = this.fb.group({ 
    vendorName: [''], 
    description: [''], 
    contactPersonName: [''], 
    contactEmail: [''], 
    contactPhone: [''], 
    websiteUrl: [''], 
    currentGoldPrice: [''], 
    totalGoldQuantity: [''] 
  });
  
  result: any = null; 
  error = ''; 
  loading = false;

  submit() {
    if (!this.vendorId) return; 
    this.loading = true; 
    this.error = ''; 
    this.result = null;
    const body = Object.fromEntries(Object.entries(this.form.value).filter(([, v]) => v));
    this.svc.updateVendor(+this.vendorId, body).subscribe({ 
      next: r => { 
        this.result = r.data; 
        this.loading = false; 
        this.notify.show('Vendor updated!'); 
      }, 
      error: e => { 
        this.error = e.error?.message || e.message || 'Failed'; 
        this.loading = false; 
      } 
    });
  }
}
