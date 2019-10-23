import { Injectable } from '@angular/core';
import {
  HttpEvent, HttpInterceptor, HttpHandler, HttpRequest
} from '@angular/common/http';

import { Observable } from 'rxjs';
import {SessionHelperService} from '../services/session-helper.service';

/**
 * Extending user session on every http request
 */
@Injectable()
export class SessionInterceptor implements HttpInterceptor {

  constructor(private sessionHelper: SessionHelperService) { }

  shouldSkip(req: HttpRequest<any>): boolean {
    return req.url.search(/\/environment/) !== -1
      || req.url.search(/\/assets/) !== -1;
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.shouldSkip(req)) {
      this.sessionHelper.refreshSessionTimer();
    }

    return next.handle(req);
  }
}
