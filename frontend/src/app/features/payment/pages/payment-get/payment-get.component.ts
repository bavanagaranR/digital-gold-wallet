import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PaymentService } from '../../services/payment.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';

@Component({
  selector: 'app-payment-get',
  standalone: true,
  imports: [CommonModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './payment-get.component.html',
  styleUrl: './payment-get.component.css'
})
export class PaymentGetComponent {
  private svc = inject(PaymentService);
  paymentId = ''; 
  result: any = null; 
  error = ''; 
  loading = false;

  submit() {
    if(parseInt(this.paymentId) <= 0)
    {
      this.error = 'Payment ID must be a greater than 0';
      return;
    }
   else if (!this.paymentId) {
      this.error = 'Payment ID is required';
      return;
    }
    
    
    this.loading = true; 
    this.error = ''; 
    this.result = null;
    this.svc.getPaymentById(+this.paymentId).subscribe({ 
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
