import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { UserFeedbackComponent } from '../../shared/user-feedback.component';
import { UserFormSupport, getControlErrorMessage, shouldShowControlError } from '../../shared/user-form-support';

@Component({
  selector: 'app-user-balance',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, PageHeaderComponent, UserFeedbackComponent],
  templateUrl: './user-balance.component.html',
  styleUrl: './user-balance.component.css'
})
export class UserBalanceComponent extends UserFormSupport {
  private svc = inject(UserService);
  private fb = inject(FormBuilder);
  override form = this.fb.group({
    userId: ['', [Validators.required, Validators.pattern(/^\d+$/), Validators.min(1)]],
  });

  balance: number | null = null;
  loading = false;

  get error(): string {
    return this.userError;
  }

  set error(value: string) {
    this.userError = value;
  }

  private userError = '';

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
    this.balance = null;
    this.svc.getUserBalance(+this.form.getRawValue().userId!).subscribe({
      next: r => {
        this.balance = (r.data as any)?.balance ?? r.data;
        this.loading = false;
      },
      error: e => {
        this.applyServerError(e);
        this.loading = false;
      }
    });
  }
}
