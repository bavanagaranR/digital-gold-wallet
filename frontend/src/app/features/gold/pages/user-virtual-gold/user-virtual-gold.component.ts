import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GoldService } from '../../services/gold.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { createEmptyGoldUiErrorState, mapGoldApiError } 
from '../../utils/gold-error.utils';

@Component({
  selector: 'app-user-virtual-gold',
  standalone: true,
  imports: [CommonModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './user-virtual-gold.component.html',
  styleUrl: './user-virtual-gold.component.css'
})
export class UserVirtualGoldComponent {

  private svc = inject(GoldService);

  userId: number | null = null;
  page = 0;
  size = 10;

  holdings: any[] = [];
  totalPages = 0;

  loading = false;

  // ✅ new structured error handling
  apiErrors = createEmptyGoldUiErrorState();

  // ✅ frontend validation helper
  isInvalidUserId(): boolean {
    return !this.userId || this.userId <= 0;
  }

  load() {

    // ❌ stop API call if invalid
    if (this.isInvalidUserId()) {
      this.apiErrors = createEmptyGoldUiErrorState();
      return;
    }

    this.loading = true;
    this.holdings = [];
    this.apiErrors = createEmptyGoldUiErrorState();

    this.svc.getUserVirtualGold(this.userId!, this.page, this.size).subscribe({

      next: r => {
        const d = r.data as any;
        this.holdings = d?.content ?? (Array.isArray(d) ? d : [d].filter(Boolean));
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