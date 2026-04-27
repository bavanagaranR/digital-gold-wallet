import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VendorService } from '../../services/vendor.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { buildResultError, createEmptyResultError, ResultErrorState } from '../../vendor-error.utils';

@Component({
  selector: 'app-branch-list',
  standalone: true,
  imports: [CommonModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './branch-list.component.html',
  styleUrl: './branch-list.component.css'
})
export class BranchListComponent {
  private svc = inject(VendorService);

  vendorId = '';
  page = 0;
  size = 10;
  branches: any[] = [];
  totalPages = 0;
  loading = false;
  submitted = false;
  resultError: ResultErrorState = createEmptyResultError();

  load() {
    this.submitted = true;
    this.resultError = createEmptyResultError();
    this.branches = [];

    if (!this.vendorId) {
      return;
    }

    if (Number(this.vendorId) <= 0) {
      return;
    }

    if (this.page < 0 || this.size < 1) {
      return;
    }

    this.loading = true;
    this.svc.getVendorBranches(+this.vendorId, this.page, this.size).subscribe({
      next: r => {
        const data = r.data as any;
        this.branches = data?.content ?? (Array.isArray(data) ? data : []);
        this.totalPages = data?.totalPages ?? 1;
        this.resultError = createEmptyResultError();
        this.loading = false;
      },
      error: e => {
        this.resultError = buildResultError(e, 'Failed to load vendor branches.');
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
