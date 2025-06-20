import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { inject } from '@angular/core';

export const userGuard: CanActivateFn = (route, state) => {
  
  const authService = inject(AuthService);
  const router = inject(Router);
  
  // Vérifier si la route est une route admin
  const isAdminRoute = route.data && route.data['adminOnly'] === true;

  // Vérifier si la route est une route utilisateur
  const isUserRoute = route.data && route.data['userOnly'] === true;


  if (isAdminRoute) {
    if (authService.isAdmin()) {
      return true;
    } else {
      router.navigate(['app-forbidden']);
      return false;
    }
  }

  if (isUserRoute) {
    if (authService.isUser() && !authService.isAdmin()) {
      return true;
    } else {
      router.navigate(['app-forbidden']);
      return false;
    }
  }
  
  

  if (authService.isAdmin())
    return true;
  else {
    router.navigate(['app-forbidden']);
    return false;
  }

  
};

