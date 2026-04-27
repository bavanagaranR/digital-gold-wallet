import { Component, inject } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VendorService } from '../../services/vendor.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { buildResultError, createEmptyResultError, ResultErrorState } from '../../vendor-error.utils';

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
  loading = false;
  submitted = false;
  resultError: ResultErrorState = createEmptyResultError();

  submit() {
    this.submitted = true;
    this.resultError = createEmptyResultError();
    this.price = null;

    if (!this.vendorId) {
      return;
    }

    if (Number(this.vendorId) <= 0) {
      return;
    }

    this.loading = true;
    this.svc.getVendorPrice(+this.vendorId).subscribe({
      next: r => {
        this.price = (r.data as any)?.price ?? r.data;
        this.resultError = createEmptyResultError();
        this.loading = false;
      },
      error: e => {
        this.resultError = buildResultError(e, 'Failed to fetch vendor price.');
        this.loading = false;
      }
    });
  }

  getVendorIdError(): string {
    if (!this.submitted) {
      return '';
    }

    if (!this.vendorId) {
      return 'Vendor ID is required.';
    }

    if (Number(this.vendorId) <= 0) {
      return 'Vendor ID must be greater than 0.';
    }

    return '';
  }
}
