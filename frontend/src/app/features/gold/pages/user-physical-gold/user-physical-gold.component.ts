import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GoldService } from '../../services/gold.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { mapGoldApiError, createEmptyGoldUiErrorState } from '../../utils/gold-error.utils';

@Component({
  selector: 'app-user-physical-gold',
  standalone: true,
  imports: [CommonModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './user-physical-gold.component.html',
  styleUrl: './user-physical-gold.component.css'
})
export class UserPhysicalGoldComponent {

  private svc = inject(GoldService);

  userId = '';
  page = 0;
  size = 10;

  records: any[] = [];
  totalPages = 0;

  loading = false;

  // ✅ NEW structured error state
  apiErrors = createEmptyGoldUiErrorState();

  // ✅ LIVE VALIDATION
  validateUserId() {
    this.apiErrors.fieldErrors = {};
  if (parseInt(this.userId) <= 0) {
      this.apiErrors.fieldErrors['userId'] = ['User ID must be greater than 0'];
      return;
    }
   else if (!this.userId) {
      this.apiErrors.fieldErrors['userId'] = ['User ID is required'];
      return;
    }

  
  }

  load() {

    this.apiErrors = createEmptyGoldUiErrorState();
    this.records = [];

    // reuse validation
    this.validateUserId();

    if (this.apiErrors.fieldErrors['userId']) {
      return;
    }

    this.loading = true;

    this.svc.getUserPhysicalGold(+this.userId, this.page, this.size).subscribe({
      next: r => {
        const d = r.data as any;

        // ✅ IMPORTANT (no regression)
        this.records = d?.content ?? (Array.isArray(d) ? d : [d].filter(Boolean));
        this.totalPages = d?.totalPages ?? 1;

        this.loading = false;
      },

      error: e => {
        this.apiErrors = mapGoldApiError(e, ['userId']);
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