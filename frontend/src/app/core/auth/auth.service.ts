import { Injectable, inject, signal } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { AuthUser, LoginRequest } from '../models/auth.model';
import { environment } from '../../../environments/environment';

// Username → accessible modules mapping (mirrors backend roles)
const USER_MODULE_MAP: Record<string, { displayName: string; modules: string[] }> = {
  admin:       { displayName: 'Admin',       modules: ['user', 'vendor', 'gold', 'payment', 'wallet', 'transaction'] },
  pavithra:    { displayName: 'Pavithra',    modules: ['user'] },
  caitlyn:     { displayName: 'Caitlyn Mary', modules: ['vendor'] },
  suba:        { displayName: 'Suba Harini', modules: ['gold'] },
  bavanagaran: { displayName: 'Bavanagaran', modules: ['payment', 'wallet'] },
  mirudhula:   { displayName: 'Mirudhula',   modules: ['transaction'] },
};

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);

  private currentUserSignal = signal<AuthUser | null>(this.loadFromStorage());

  get currentUser() { return this.currentUserSignal(); }
  get isLoggedIn()  { return !!this.currentUserSignal(); }
  get token()       { return this.currentUserSignal()?.token ?? null; }

  login(req: LoginRequest): Observable<AuthUser> {
    // Encode credentials as HTTP Basic token
    const basicToken = btoa(`${req.username}:${req.password}`);
    const headers = new HttpHeaders({ Authorization: `Basic ${basicToken}` });

    // Probe any authenticated endpoint to validate credentials
    return this.http
      .get<unknown>(`${environment.apiBaseUrl}/api/v1/auth/ping`, { headers })
      .pipe(
        map(() => {
          const profile = USER_MODULE_MAP[req.username];
          if (!profile) throw new Error('User not found in module map');

          const user: AuthUser = {
            username:    req.username,
            displayName: profile.displayName,
            role:        req.username === 'admin' ? 'admin' : 'developer',
            modules:     profile.modules,
            token:       basicToken,        // stored and forwarded by interceptor
          };
          localStorage.setItem('dgw_user', JSON.stringify(user));
          this.currentUserSignal.set(user);
          return user;
        }),
        catchError(() => throwError(() => new Error('Invalid username or password')))
      );
  }

  logout() {
    localStorage.removeItem('dgw_user');
    this.currentUserSignal.set(null);
    this.router.navigate(['/login']);
  }

  canAccessModule(module: string): boolean {
    const user = this.currentUserSignal();
    if (!user) return false;
    if (user.role === 'admin') return true;
    return user.modules.includes(module);
  }

  private loadFromStorage(): AuthUser | null {
    try {
      const raw = localStorage.getItem('dgw_user');
      return raw ? JSON.parse(raw) : null;
    } catch { return null; }
  }
}
