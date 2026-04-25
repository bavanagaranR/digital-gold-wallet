import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { GoldService } from '../../services/gold.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';
import { NotificationService } from '../../../../core/services/notification.service';

@Component({
  selector: 'app-gold-convert',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './gold-convert.component.html',
  styleUrl: './gold-convert.component.css'
})
export class GoldConvertComponent {
  private svc = inject(GoldService); 
  private notify = inject(NotificationService); 
  private fb = inject(FormBuilder);
  
  form = this.fb.group({ 
    userId: ['', Validators.required], 
    branchId: ['', Validators.required], 
    quantity: ['', Validators.required], 
    deliveryAddressId: ['', Validators.required] 
  });
  
  result: any = null; 
  error = ''; 
  loading = false;

  submit() {
    this.loading = true; 
    this.error = ''; 
    this.result = null;
    this.svc.convertToPhysical(this.form.value as any).subscribe({
      next: r => { 
        this.result = r.data ? { ...r.data, message: r.message } : r; 
        this.loading = false; 
        this.notify.show('Gold converted to physical successfully'); 
      },
      error: e => { 
        this.error = e.error?.message || e.message || 'Conversion failed'; 
        this.loading = false; 
      }
    });
  }
}
