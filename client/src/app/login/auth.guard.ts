import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = () => {
  const router = inject(Router);

  // TEMP login check (we'll replace with real backend session later)
  const loggedIn = localStorage.getItem('loggedIn') === 'true';

  if (!loggedIn) {
    router.navigate(['']);
    return false;
  }

  return true;
};
