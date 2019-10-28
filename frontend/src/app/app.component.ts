import { Component, ChangeDetectionStrategy} from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Router, NavigationEnd } from '@angular/router';
import { EnvironmentService } from './services/environment.service';
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
              private sessionHelper: SessionHelperService) {
    translate.addLangs(['en', 'et']);
    let currentLang = translate.getBrowserLang() || 'et';
    if (translate.getLangs().indexOf(currentLang) === -1) {
      currentLang = 'et';
    }

    // 'en' not supported yet
    translate.use('et');

    const googleAnalyticsId = this.environmentService.globalEnvironment.getGoogleAnalyticsId();

    this.router.routeReuseStrategy.shouldReuseRoute = function(future, curr){
      return false;
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
  }
}
