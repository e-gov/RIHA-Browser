import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';

export interface CsrfTokenResponse {
  token: string;
  headerName: string;
  parameterName: string;
}

@Injectable({
  providedIn: 'root',
})
export class CsrfTokenService {
  private tokenSubject = new BehaviorSubject<string | null>(null);
  private headerNameSubject = new BehaviorSubject<string>('X-XSRF-TOKEN');

  constructor(private http: HttpClient) {}

  getCsrfToken(): Observable<CsrfTokenResponse> {
    return this.http.get<CsrfTokenResponse>('/api/v1/csrf/token').pipe(
      tap(response => {
        this.tokenSubject.next(response.token);
        this.headerNameSubject.next(response.headerName);
      }),
    );
  }

  getCurrentToken(): string | null {
    return this.tokenSubject.value;
  }

  getCurrentHeaderName(): string {
    return this.headerNameSubject.value;
  }

  refreshToken(): Observable<CsrfTokenResponse> {
    return this.getCsrfToken();
  }
}
