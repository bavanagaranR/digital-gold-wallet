import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VendorService } from '../../services/vendor.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { buildResultError, createEmptyResultError, ResultErrorState } from '../../vendor-error.utils';

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
  loading = false;
  submitted = false;
  resultError: ResultErrorState = createEmptyResultError();

  submit() {
    this.submitted = true;
    this.resultError = createEmptyResultError();
    this.result = null;

    if (!this.vendorId) {
      return;
    }

    if (Number(this.vendorId) <= 0) {
      return;
    }

    this.loading = true;
    this.svc.getVendorById(+this.vendorId).subscribe({
      next: r => {
        this.result = r.data;
        this.resultError = createEmptyResultError();
        this.loading = false;
      },
      error: e => {
        this.resultError = buildResultError(e, 'Vendor not found.');
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
