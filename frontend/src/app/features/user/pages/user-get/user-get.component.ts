import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { ResultViewerComponent } from '../../../../shared/components/result-viewer/result-viewer.component';

@Component({
  selector: 'app-user-get',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, PageHeaderComponent, ResultViewerComponent],
  templateUrl: './user-get.component.html',
  styleUrl: './user-get.component.css'
})
export class UserGetComponent {
  private svc = inject(UserService);
  userId = ''; 
  result: any = null; 
  error = ''; 
  loading = false;

  submit() {
    if (!this.userId) return;
    this.loading = true; 
    this.error = ''; 
    this.result = null;
    this.svc.getUserById(+this.userId).subscribe({
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
