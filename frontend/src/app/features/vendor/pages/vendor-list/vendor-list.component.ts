import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VendorService } from '../../services/vendor.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { buildResultError, createEmptyResultError, ResultErrorState } from '../../vendor-error.utils';

@Component({
  selector: 'app-vendor-list',
  standalone: true,
  imports: [CommonModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './vendor-list.component.html',
  styleUrl: './vendor-list.component.css'
})
export class VendorListComponent {
  private svc = inject(VendorService);

  page = 0;
  size = 10;
  vendors: any[] = [];
  totalPages = 0;
  totalElements = 0;
  loading = false;
  submitted = false;
  resultError: ResultErrorState = createEmptyResultError();

  load() {
    this.submitted = true;
    this.resultError = createEmptyResultError();
    this.vendors = [];

    if (this.page < 0 || this.size < 1) {
      return;
    }

    this.loading = true;
    this.svc.getAllVendors(this.page, this.size).subscribe({
      next: r => {
        const data = r.data as any;
        this.vendors = data?.content ?? (Array.isArray(data) ? data : []);
        this.totalPages = data?.totalPages ?? 1;
        this.totalElements = data?.totalElements ?? this.vendors.length;
        this.resultError = createEmptyResultError();
        this.loading = false;
      },
      error: e => {
        this.resultError = buildResultError(e, 'Failed to load vendors.');
        this.loading = false;
      }
    });
  }

  prev() {
    if (this.page > 0) {
      this.page--;
      this.load();
    }
  }

  next() {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.load();
    }
  }

  getPaginationError(): string {
    if (!this.submitted) {
      return '';
    }

    if (this.page < 0) {
      return 'Page must be 0 or greater.';
    }

    if (this.size < 1) {
      return 'Size must be at least 1.';
    }

    return '';
  }
}
