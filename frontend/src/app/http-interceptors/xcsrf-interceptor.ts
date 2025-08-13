import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpErrorResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, throwError} from 'rxjs';
import {catchError, switchMap} from 'rxjs/operators';
import {CsrfTokenService} from '../services/csrf-token.service';

@Injectable()
export class HttpXsrfInterceptor implements HttpInterceptor {

  constructor(private csrfTokenService: CsrfTokenService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Skip CSRF token for GET requests and the CSRF token endpoint itself
    if (req.method === 'GET' || req.url.includes('/csrf/token')) {
      return next.handle(req);
    }

    const token = this.csrfTokenService.getCurrentToken();
    const headerName = this.csrfTokenService.getCurrentHeaderName();

    if (token) {
      // Add the token to the request
      const tokenizedReq = req.clone({
        headers: req.headers.set(headerName, token)
      });
      return next.handle(tokenizedReq).pipe(
        catchError((error: HttpErrorResponse) => {
          // If we get a 403 error, it might be due to an expired CSRF token
          if (error.status === 403) {
            // Try to refresh the token and retry the request
            return this.csrfTokenService.refreshToken().pipe(
              switchMap(tokenResponse => {
                const retryReq = req.clone({
                  headers: req.headers.set(tokenResponse.headerName, tokenResponse.token)
                });
                return next.handle(retryReq);
              })
            );
          }
          return throwError(error);
        })
      );
    } else {
      // No token available, try to get one and retry
      return this.csrfTokenService.getCsrfToken().pipe(
        switchMap(tokenResponse => {
          const tokenizedReq = req.clone({
            headers: req.headers.set(tokenResponse.headerName, tokenResponse.token)
          });
          return next.handle(tokenizedReq);
        })
      );
    }
  }
}
