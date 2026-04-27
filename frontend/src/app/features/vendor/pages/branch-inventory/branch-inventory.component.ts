import { Component, inject } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VendorService } from '../../services/vendor.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';

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
    this.inventory = null;
    this.svc.getBranchInventory(+this.branchId).subscribe({ 
      next: r => { 
        this.inventory = (r.data as any)?.inventoryGrams ?? r.data; 
        this.loading = false; 
      }, 
      error: e => { 
        this.error = e.error?.message || e.message || 'Failed'; 
        this.loading = false; 
      } 
    });
  }
}
