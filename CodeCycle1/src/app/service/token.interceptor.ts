import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const toExclude = "/login";

  if(req.url.search(toExclude) == -1){
    let jwt = authService.getToken();
    let reqWWithToken = req.clone( {
      setHeaders: { Authorization : "Bearer "+jwt}
    });
    return next(reqWWithToken);
  }

  return next(req);
};
