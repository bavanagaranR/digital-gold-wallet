import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VendorService } from '../../services/vendor.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { buildResultError, createEmptyResultError, ResultErrorState } from '../../vendor-error.utils';

@Component({
  selector: 'app-branch-get',
  standalone: true,
  imports: [CommonModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './branch-get.component.html',
  styleUrl: './branch-get.component.css'
})
export class BranchGetComponent {
  private svc = inject(VendorService);

  branchId = '';
  result: any = null;
  loading = false;
  submitted = false;
  resultError: ResultErrorState = createEmptyResultError();

  submit() {
    this.submitted = true;
    this.resultError = createEmptyResultError();
    this.result = null;

    if (!this.branchId) {
      return;
    }

    if (Number(this.branchId) <= 0) {
      return;
    }

    this.loading = true;
    this.svc.getBranchById(+this.branchId).subscribe({
      next: r => {
        this.result = r.data;
        this.resultError = createEmptyResultError();
        this.loading = false;
      },
      error: e => {
        this.resultError = buildResultError(e, 'Branch not found.');
        this.loading = false;
      }
    });
  }

  getBranchIdError(): string {
    if (!this.submitted) {
      return '';
    }

    if (!this.branchId) {
      return 'Branch ID is required.';
    }

    if (Number(this.branchId) <= 0) {
      return 'Branch ID must be greater than 0.';
    }

    return '';
  }
}
