import { inject } from '@angular/core';
import { CanActivateFn, ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const module = route.data['module'] as string;
  if (!auth.isLoggedIn) { router.navigate(['/login']); return false; }
  if (auth.canAccessModule(module)) return true;
  router.navigate(['/home']);
  return false;
};
