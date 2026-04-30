import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { NotificationService } from '../../../../core/services/notification.service';
import { UserFeedbackComponent } from '../../shared/user-feedback.component';
import { UserFormSupport, getControlErrorMessage, shouldShowControlError } from '../../shared/user-form-support';

@Component({
  selector: 'app-user-update',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, PageHeaderComponent, UserFeedbackComponent],
  templateUrl: './user-update.component.html',
  styleUrl: './user-update.component.css'
})
export class UserUpdateComponent extends UserFormSupport {
  private svc = inject(UserService);
  private fb = inject(FormBuilder);
  private notify = inject(NotificationService);
  
  override form = this.fb.group({
    userId: ['', [Validators.required, Validators.pattern(/^\d+$/), Validators.min(1)]],
    name: ['', [Validators.pattern(/^[A-Za-z]+$/), Validators.minLength(2), Validators.maxLength(100)]],
    email: ['', [Validators.pattern(/^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$/), Validators.maxLength(100)]],
    addressId: ['', [Validators.pattern(/^\d+$/), Validators.min(1)]]
  });
  
  result: any = null;
  loading = false;

  get error(): string {
    return this.userError;
  }

  set error(value: string) {
    this.userError = value;
  }

  private userError = '';

  submit() {
    this.startSubmit();
    if (this.form.invalid) {
      return;
    }
    this.loading = true;
    this.error = '';
    this.result = null;
    const { userId, ...body } = this.form.getRawValue();
    const requestBody = Object.fromEntries(Object.entries(body).filter(([, v]) => v !== '' && v != null));
    this.svc.updateUser(+userId!, requestBody).subscribe({
      next: r => {
        this.result = r.data;
        this.loading = false;
        this.notify.show('User updated!');
      },
      error: e => {
        this.applyServerError(e);
        this.loading = false;
      }
    });
  }

  controlErrorMessage(field: string): string {
    return getControlErrorMessage(this.form.get(field), field);
  }

  showControlError(field: string): boolean {
    return shouldShowControlError(this.form.get(field), this.submitted);
  }
}
