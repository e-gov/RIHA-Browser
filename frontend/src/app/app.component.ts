import { Component, ChangeDetectionStrategy} from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { PageScrollConfig } from 'ng2-page-scroll';
import { Router, NavigationEnd } from '@angular/router';
import { EnvironmentService } from './services/environment.service';
import { HttpInterceptorService } from 'ng-http-interceptor';
import { SessionHelperService } from './services/session-helper.service';

declare let ga: Function;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(private translate: TranslateService,
              private environmentService: EnvironmentService,
              private router: Router,
              private sessionHelper: SessionHelperService,
              private httpInterceptor: HttpInterceptorService) {
    translate.addLangs(['en', 'et']);
    let currentLang = translate.getBrowserLang() || 'et';
    if (translate.getLangs().indexOf(currentLang) === -1) {
      currentLang = 'et';
    }

    // 'en' not supported yet
    translate.use('et');

    PageScrollConfig.defaultDuration = 400;

    let googleAnalyticsId = this.environmentService.globalEnvironment.getGoogleAnalyticsId();

    this.router.routeReuseStrategy.shouldReuseRoute = function(future, curr){
      if (router.url.split('/')[2] == 'Vaata' && (future.fragment || future.url.length > 0)){
        return true
      } else {
        return false;
      }
    };

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        if (googleAnalyticsId){
          // USED FOR GOOGLE ANALYTICS
          ga('set', 'page', event.urlAfterRedirects);
          ga('send', 'pageview');
        }
        this.environmentService.addLastVisitedLocation(event.urlAfterRedirects);
      }
    });

    sessionHelper.refreshSessionTimer();
    this.httpInterceptor.request().addInterceptor((data, method) => {
      sessionHelper.refreshSessionTimer();
      return data;
    });

  }
}
