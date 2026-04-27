import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { UserFeedbackComponent } from '../../shared/user-feedback.component';
import { UserFormSupport, getControlErrorMessage, shouldShowControlError } from '../../shared/user-form-support';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, PageHeaderComponent, UserFeedbackComponent],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css'
})
export class UserListComponent extends UserFormSupport {
  private svc = inject(UserService);
  private fb = inject(FormBuilder);
  override form = this.fb.group({
    page: [0, [Validators.required, Validators.min(0), Validators.pattern(/^\d+$/)]],
    size: [10, [Validators.required, Validators.min(1), Validators.max(100), Validators.pattern(/^\d+$/)]],
  });

  users: any[] = [];
  totalPages = 0;
  totalElements = 0;
  loading = false;

  controlErrorMessage(field: string): string {
    return getControlErrorMessage(this.form.get(field), field);
  }

  showControlError(field: string): boolean {
    return shouldShowControlError(this.form.get(field), this.submitted);
  }

  get page(): number {
    return Number(this.form.getRawValue().page);
  }

  get size(): number {
    return Number(this.form.getRawValue().size);
  }

  load() {
    this.startSubmit();
    if (this.form.invalid) {
      return;
    }
    this.loading = true;
    this.users = [];
    const { page, size } = this.form.getRawValue();
    this.svc.getAllUsers(Number(page), Number(size)).subscribe({
      next: r => {
        const d = r.data as any;
        this.users = d?.content ?? (Array.isArray(d) ? d : []);
        this.totalPages = d?.totalPages ?? 1;
        this.totalElements = d?.totalElements ?? this.users.length;
        this.loading = false;
      },
      error: e => {
        this.applyServerError(e);
        this.loading = false;
      }
    });
  }

  prev() { 
    const page = Number(this.form.getRawValue().page);
    if (page > 0) {
      this.form.patchValue({ page: page - 1 });
      this.load(); 
    } 
  }

  next() { 
    const page = Number(this.form.getRawValue().page);
    if (page < this.totalPages - 1) {
      this.form.patchValue({ page: page + 1 });
      this.load(); 
    } 
  }
}
