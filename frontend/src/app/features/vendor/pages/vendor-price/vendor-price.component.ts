import { Component, inject } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VendorService } from '../../services/vendor.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';

@Component({
  selector: 'app-vendor-price',
  standalone: true,
  imports: [CommonModule, FormsModule, DecimalPipe, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './vendor-price.component.html',
  styleUrl: './vendor-price.component.css'
})
export class VendorPriceComponent {
  private svc = inject(VendorService);
  vendorId = ''; 
  price: number | null = null; 
  error = ''; 
  loading = false;

  submit() {
    if (!this.vendorId) return; 
    this.loading = true; 
    this.error = ''; 
    this.price = null;
    this.svc.getVendorPrice(+this.vendorId).subscribe({ 
      next: r => { 
        this.price = (r.data as any)?.price ?? r.data; 
        this.loading = false; 
      }, 
      error: e => { 
        this.error = e.error?.message || e.message || 'Failed'; 
        this.loading = false; 
      } 
    });
  }
}
