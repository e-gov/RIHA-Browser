import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, of} from 'rxjs';
import {catchError, switchMap} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class CustomCsrfInterceptor implements HttpInterceptor {
  private csrfToken: string | null = null;
  private csrfHeaderName: string = 'X-CSRF-TOKEN';

  constructor(private http: HttpClient) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Skip CSRF for GET, HEAD, OPTIONS, TRACE requests
    if (req.method === 'GET' || req.method === 'HEAD' || req.method === 'OPTIONS' || req.method === 'TRACE') {
      return next.handle(req);
    }

    // Skip CSRF for the CSRF token endpoint itself
    if (req.url.includes('/api/v1/csrf/token')) {
      return next.handle(req);
    }

    // If we already have a token, use it
    if (this.csrfToken) {
      const csrfReq = req.clone({
        headers: req.headers.set(this.csrfHeaderName, this.csrfToken)
      });
      return next.handle(csrfReq);
    }

    // Fetch CSRF token from backend
    return this.fetchCsrfToken().pipe(
      switchMap(() => {
        const csrfReq = req.clone({
          headers: req.headers.set(this.csrfHeaderName, this.csrfToken!)
        });
        return next.handle(csrfReq);
      }),
      catchError((error) => {
        console.error('Failed to fetch CSRF token:', error);
        // Continue without CSRF token if fetch fails
        return next.handle(req);
      })
    );
  }

  private fetchCsrfToken(): Observable<any> {
    return this.http.get<any>('/api/v1/csrf/token').pipe(
      switchMap((response) => {
        this.csrfToken = response.token;
        this.csrfHeaderName = response.headerName || 'X-CSRF-TOKEN';
        return of(response);
      })
    );
  }
}
