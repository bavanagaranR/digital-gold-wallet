import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { NotificationService } from '../../../../core/services/notification.service';
import { UserFeedbackComponent } from '../../shared/user-feedback.component';
import { UserFormSupport, getControlErrorMessage, shouldShowControlError } from '../../shared/user-form-support';

@Component({
  selector: 'app-address-update',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, PageHeaderComponent, UserFeedbackComponent],
  templateUrl: './address-update.component.html',
  styleUrl: './address-update.component.css'
})
export class AddressUpdateComponent extends UserFormSupport {
  private svc = inject(UserService);
  private fb = inject(FormBuilder);
  private notify = inject(NotificationService);
  
  override form = this.fb.group({
    addressId: ['', [Validators.required, Validators.pattern(/^\d+$/), Validators.min(1)]],
    street: ['', [Validators.maxLength(255)]],
    city: ['', [Validators.maxLength(100)]],
    state: ['', [Validators.maxLength(100)]],
    postalCode: ['', [Validators.pattern(/^[1-9][0-9]{5}$/)]],
    country: ['', [Validators.maxLength(100)]]
  });
  
  result: any = null;
  loading = false;

  controlErrorMessage(field: string): string {
    return getControlErrorMessage(this.form.get(field), field);
  }

  showControlError(field: string): boolean {
    return shouldShowControlError(this.form.get(field), this.submitted);
  }

  submit() {
    this.startSubmit();
    if (this.form.invalid) {
      return;
    }else if (parseInt(this.addressId)<=0 ) {
      this.error = 'Address ID must be a greater than 0';
      return;
    }
    this.loading = true;
    this.result = null;
    const { addressId, ...body } = this.form.getRawValue();
    const requestBody = Object.fromEntries(Object.entries(body).filter(([, v]) => v !== '' && v != null));
    this.svc.updateAddress(+addressId!, requestBody).subscribe({
      next: r => {
        this.result = r.data;
        this.loading = false;
        this.notify.show('Address updated!');
      },
      error: e => {
        this.applyServerError(e);
        this.loading = false;
      }
    });
  }
}
