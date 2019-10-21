import {HTTP_INTERCEPTORS} from '@angular/common/http';

import {SessionInterceptor} from './session-interceptor';

/** Http interceptor providers in outside-in order */
export const httpInterceptorProviders = [
  {provide: HTTP_INTERCEPTORS, useClass: SessionInterceptor, multi: true},
];
