import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormsModule } from '@angular/forms';
import { VendorService } from '../../services/vendor.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { NotificationService } from '../../../../core/services/notification.service';

@Component({
  selector: 'app-branch-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './branch-create.component.html',
  styleUrl: './branch-create.component.css'
})
export class BranchCreateComponent {
  private svc = inject(VendorService); 
  private fb = inject(FormBuilder); 
  private notify = inject(NotificationService);
  
  vendorId = ''; 
  form = this.fb.group({ 
    addressId: ['', Validators.required], 
    quantity: [''] 
  });
  
  result: any = null; 
  error = ''; 
  loading = false;

  submit() {
    if (!this.vendorId) return; 
    this.loading = true; 
    this.error = ''; 
    this.result = null;
    this.svc.createBranch(+this.vendorId, this.form.value as any).subscribe({ 
      next: r => { 
        this.result = r.data; 
        this.loading = false; 
        this.notify.show('Branch created!'); 
      }, 
      error: e => { 
        this.error = e.error?.message || e.message || 'Failed'; 
        this.loading = false; 
      } 
    });
  }
}
