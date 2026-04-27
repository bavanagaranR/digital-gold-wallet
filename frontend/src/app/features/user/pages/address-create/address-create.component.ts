import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { NotificationService } from '../../../../core/services/notification.service';
import { UserFeedbackComponent } from '../../shared/user-feedback.component';
import { UserFormSupport, getControlErrorMessage, shouldShowControlError } from '../../shared/user-form-support';

@Component({
  selector: 'app-address-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, PageHeaderComponent, UserFeedbackComponent],
  templateUrl: './address-create.component.html',
  styleUrl: './address-create.component.css'
})
export class AddressCreateComponent extends UserFormSupport {
  private svc = inject(UserService);
  private notify = inject(NotificationService);
  private fb = inject(FormBuilder);
  
  override form = this.fb.group({
    street: ['', [Validators.required, Validators.maxLength(255)]],
    city: ['', [Validators.required, Validators.maxLength(100)]],
    state: ['', [Validators.required, Validators.maxLength(100)]],
    postalCode: ['', [Validators.required, Validators.pattern(/^[1-9][0-9]{5}$/)]],
    country: ['India', [Validators.required, Validators.maxLength(100)]]
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
    }
    this.loading = true;
    this.result = null;
    this.svc.createAddress(this.form.getRawValue() as any).subscribe({
      next: r => {
        this.result = r.data;
        this.loading = false;
        this.notify.show('Address created!');
      },
      error: e => {
        this.applyServerError(e);
        this.loading = false;
      }
    });
  }
}
