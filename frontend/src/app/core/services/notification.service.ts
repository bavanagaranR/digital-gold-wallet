import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  show(message: string, type: 'success' | 'error' = 'success', duration = 3000) {
    const el = document.createElement('div');
    el.className = type === 'success' ? 'toast-success' : 'toast-error';
    el.textContent = message;
    document.body.appendChild(el);
    setTimeout(() => el.remove(), duration);
  }
}
