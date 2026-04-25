import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const token = auth.token;
  // Sends HTTP Basic credentials on every API request (stored as base64 in token field)
  if (token) {
    const cloned = req.clone({
      setHeaders: { Authorization: `Basic ${token}` }
    });
    return next(cloned);
  }
  return next(req);
};

