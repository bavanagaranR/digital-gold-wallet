import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VendorService } from '../../services/vendor.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';

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
  error = ''; 
  loading = false;

  load() {
    if (!this.vendorId) return; 
    this.loading = true; 
    this.error = ''; 
    this.branches = [];
    this.svc.getVendorBranches(+this.vendorId, this.page, this.size).subscribe({
      next: r => { 
        const d = r.data as any; 
        this.branches = d?.content ?? (Array.isArray(d) ? d : []); 
        this.totalPages = d?.totalPages ?? 1; 
        this.loading = false; 
      },
      error: e => { 
        this.error = e.error?.message || e.message || 'Failed'; 
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
