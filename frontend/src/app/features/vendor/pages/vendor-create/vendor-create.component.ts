import { Component, inject } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { VendorService } from '../../services/vendor.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { NotificationService } from '../../../../core/services/notification.service';

@Component({
  selector: 'app-vendor-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, PageHeaderComponent, ResultViewerComponent, DecimalPipe],
  templateUrl: './vendor-create.component.html',
  styleUrl: './vendor-create.component.css'
})
export class VendorCreateComponent {
  private svc = inject(VendorService); 
  private notify = inject(NotificationService); 
  private fb = inject(FormBuilder);
  
  form = this.fb.group({ 
    vendorName: ['', Validators.required], 
    description: [''], 
    contactPersonName: [''], 
    contactEmail: ['', [Validators.required, Validators.email]], 
    contactPhone: ['', Validators.required], 
    websiteUrl: [''], 
    currentGoldPrice: ['', Validators.required], 
    totalGoldQuantity: [''] 
  });
  
  result: any = null; 
  error = ''; 
  loading = false;

  submit() {
    this.loading = true; 
    this.error = ''; 
    this.result = null;
    this.svc.createVendor(this.form.value as any).subscribe({
      next: r => { 
        this.result = r.data; 
        this.loading = false; 
        this.notify.show('Vendor created!'); 
      },
      error: e => { 
        this.error = e.error?.message || e.message || 'Failed'; 
        this.loading = false; 
      }
    });
  }
}
