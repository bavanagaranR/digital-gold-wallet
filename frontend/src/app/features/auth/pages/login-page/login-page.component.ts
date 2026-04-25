import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../../core/auth/auth.service';
import { NotificationService } from '../../../../core/services/notification.service';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css'
})
export class LoginPageComponent {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);
  private notify = inject(NotificationService);

  form = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  loading = false;
  error = '';

  credentials = [
    { user: 'admin',       pass: 'admin123',      label: 'All Modules' },
    { user: 'pavithra',    pass: 'pavi123',        label: 'User Module' },
    { user: 'caitlyn',     pass: 'cait123',        label: 'Vendor Module' },
    { user: 'suba',        pass: 'suba123',        label: 'Gold Module' },
    { user: 'bavanagaran', pass: 'bavan123',       label: 'Payment & Wallet' },
    { user: 'mirudhula',   pass: 'mirudhula123',   label: 'Transaction' },
  ];

  fillCred(user: string, pass: string) {
    this.form.setValue({ username: user, password: pass });
  }

  onSubmit() {
    if (this.form.invalid) return;
    this.loading = true;
    this.error = '';
    const { username, password } = this.form.value;
    this.auth.login({ username: username!, password: password! }).subscribe({
      next: () => {
        this.notify.show('Login successful! Welcome.');
        this.router.navigate(['/home']);
      },
      error: (e) => {
        this.error = e.message;
        this.loading = false;
      }
    });
  }
}