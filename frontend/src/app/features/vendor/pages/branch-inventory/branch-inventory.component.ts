import { Component, inject } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VendorService } from '../../services/vendor.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { buildResultError, createEmptyResultError, ResultErrorState } from '../../vendor-error.utils';

@Component({
  selector: 'app-branch-inventory',
  standalone: true,
  imports: [CommonModule, FormsModule, DecimalPipe, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './branch-inventory.component.html',
  styleUrl: './branch-inventory.component.css'
})
export class BranchInventoryComponent {
  private svc = inject(VendorService);

  branchId = '';
  inventory: number | null = null;
  loading = false;
  submitted = false;
  resultError: ResultErrorState = createEmptyResultError();

  submit() {
    this.submitted = true;
    this.resultError = createEmptyResultError();
    this.inventory = null;

    if (!this.branchId) {
      return;
    }

    if (Number(this.branchId) <= 0) {
      return;
    }

    this.loading = true;
    this.svc.getBranchInventory(+this.branchId).subscribe({
      next: r => {
        this.inventory = (r.data as any)?.inventoryGrams ?? r.data;
        this.resultError = createEmptyResultError();
        this.loading = false;
      },
      error: e => {
        this.resultError = buildResultError(e, 'Failed to fetch branch inventory.');
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
