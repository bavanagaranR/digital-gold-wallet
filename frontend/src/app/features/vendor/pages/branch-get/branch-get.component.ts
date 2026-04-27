import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VendorService } from '../../services/vendor.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';

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
  error = ''; 
  loading = false;

  submit() {
    if (parseInt(this.branchId)<=0 ) {
      this.error = 'BranchId must be a greater than 0';
      return;
    
    }
  
   else if (!this.branchId) {
      this.error = 'BranchId  is required *';
      return;
    }; 
    this.loading = true; 
    this.error = ''; 
    this.result = null;
    this.svc.getBranchById(+this.branchId).subscribe({ 
      next: r => { 
        this.result = r.data; 
        this.loading = false; 
      }, 
      error: e => { 
        this.error = e.error?.message || e.message || 'Not found'; 
        this.loading = false; 
      } 
    });
  }
}
