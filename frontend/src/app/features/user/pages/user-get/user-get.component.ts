import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { UserFeedbackComponent } from '../../shared/user-feedback.component';
import { UserFormSupport, getControlErrorMessage, shouldShowControlError } from '../../shared/user-form-support';

@Component({
  selector: 'app-user-get',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, PageHeaderComponent, UserFeedbackComponent],
  templateUrl: './user-get.component.html',
  styleUrl: './user-get.component.css'
})
export class UserGetComponent extends UserFormSupport {
  private svc = inject(UserService);
  private fb = inject(FormBuilder);
  override form = this.fb.group({
    userId: ['', [Validators.required, Validators.pattern(/^\d+$/), Validators.min(1)]],
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
    this.svc.getUserById(+this.form.getRawValue().userId!).subscribe({
      next: r => {
        this.result = r.data;
        this.loading = false;
      },
      error: e => {
        this.applyServerError(e);
        this.loading = false;
      }
    });
  }
}
