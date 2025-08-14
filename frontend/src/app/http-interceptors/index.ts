import { HTTP_INTERCEPTORS } from '@angular/common/http';

import {SessionInterceptor} from './session-interceptor';
import {CustomCsrfInterceptor} from './custom-csrf-interceptor';

/** Http interceptor providers in outside-in order */
export const httpInterceptorProviders = [
  {provide: HTTP_INTERCEPTORS, useClass: CustomCsrfInterceptor, multi: true},
  {provide: HTTP_INTERCEPTORS, useClass: SessionInterceptor, multi: true},
];
