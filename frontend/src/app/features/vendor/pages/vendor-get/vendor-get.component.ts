import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VendorService } from '../../services/vendor.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';

@Component({
  selector: 'app-vendor-get',
  standalone: true,
  imports: [CommonModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './vendor-get.component.html',
  styleUrl: './vendor-get.component.css'
})
export class VendorGetComponent {
  private svc = inject(VendorService);
  vendorId = ''; 
  result: any = null; 
  error = ''; 
  loading = false;

  submit() {
    if (!this.vendorId) return; 
    this.loading = true; 
    this.error = ''; 
    this.result = null;
    this.svc.getVendorById(+this.vendorId).subscribe({ 
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
