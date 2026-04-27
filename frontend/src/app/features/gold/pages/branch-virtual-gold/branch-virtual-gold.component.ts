import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GoldService } from '../../services/gold.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { mapGoldApiError, createEmptyGoldUiErrorState } from '../../utils/gold-error.utils';

@Component({
  selector: 'app-branch-virtual-gold',
  standalone: true,
  imports: [CommonModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './branch-virtual-gold.component.html',
  styleUrl: './branch-virtual-gold.component.css'
})
export class BranchVirtualGoldComponent {

  private svc = inject(GoldService);

  branchId = '';
  page = 0;
  size = 10;

  holdings: any[] = [];
  totalPages = 0;

  loading = false;

  apiErrors = createEmptyGoldUiErrorState();

  // ✅ LIVE VALIDATION FUNCTION
  validateBranchId() {
    // reset only field errors (don't touch footer errors)
    this.apiErrors.fieldErrors = {};
  if (parseInt(this.branchId) <= 0) {
      this.apiErrors.fieldErrors['branchId'] = ['Branch ID must be greater than 0'];
      return;
    }
    else if (!this.branchId) {
      this.apiErrors.fieldErrors['branchId'] = ['Branch ID is required'];
      return;
    }
  
  }

  load() {

    // reset full error state
    this.apiErrors = createEmptyGoldUiErrorState();
    this.holdings = [];

    // ✅ reuse validation
    this.validateBranchId();

    // stop API if invalid
    if (this.apiErrors.fieldErrors['branchId']) {
      return;
    }

    this.loading = true;

    this.svc.getBranchVirtualGold(+this.branchId, this.page, this.size).subscribe({
      next: r => {
        const d = r.data as any;

        this.holdings = d?.content ?? (Array.isArray(d) ? d : [d].filter(Boolean));
        this.totalPages = d?.totalPages ?? 1;

        this.loading = false;
      },

      error: e => {
        this.apiErrors = mapGoldApiError(e, ['branchId']);
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
}