import {HTTP_INTERCEPTORS} from '@angular/common/http';

import {SessionInterceptor} from './session-interceptor';
import {HttpXsrfInterceptor} from './xcsrf-interceptor';

/** Http interceptor providers in outside-in order */
export const httpInterceptorProviders = [
  {provide: HTTP_INTERCEPTORS, useClass: HttpXsrfInterceptor, multi: true},
  {provide: HTTP_INTERCEPTORS, useClass: SessionInterceptor, multi: true},
];
