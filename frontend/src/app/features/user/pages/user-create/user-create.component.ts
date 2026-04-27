import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { NotificationService } from '../../../../core/services/notification.service';
import { UserFeedbackComponent } from '../../shared/user-feedback.component';
import { UserFormSupport, getControlErrorMessage, shouldShowControlError } from '../../shared/user-form-support';

@Component({
  selector: 'app-user-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, PageHeaderComponent, UserFeedbackComponent],
  templateUrl: './user-create.component.html',
  styleUrl: './user-create.component.css'
})
export class UserCreateComponent extends UserFormSupport {
  private svc = inject(UserService);
  private notify = inject(NotificationService);
  private fb = inject(FormBuilder);
  
  override form = this.fb.group({
    name: ['', [Validators.required, Validators.pattern(/^[A-Za-z]+$/), Validators.minLength(2), Validators.maxLength(100)]],
    email: ['', [Validators.required, Validators.pattern(/^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$/), Validators.maxLength(100)]],
    addressId: ['', [Validators.required, Validators.pattern(/^\d+$/), Validators.min(1)]]
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
    this.svc.createUser(this.form.getRawValue() as any).subscribe({
      next: r => {
        this.result = r.data;
        this.loading = false;
        this.notify.show('User created successfully!');
      },
      error: e => {
        this.applyServerError(e);
        this.loading = false;
      }
    });
  }
}
