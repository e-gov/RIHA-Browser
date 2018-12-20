import {Injectable} from '@angular/core';
import {Http} from '@angular/http';
import {Environment} from '../models/environment';
import {User} from '../models/user';
import {UserMatrix} from '../models/user-matrix';

declare let ga: Function;

@Injectable()
export class EnvironmentService {

  private environmentUrl = '/api/v1/environment';
  private userInfoUrl = '/api/v1/user';

  public globalEnvironment: any;

  private lastVisitedLocations = [null, null];

  public addLastVisitedLocation(l){
    this.lastVisitedLocations.unshift(l);
    this.lastVisitedLocations.pop();
  }

  public getPrevVisitedLocation(){
    if (this.lastVisitedLocations[1]){
      return this.lastVisitedLocations[1].split('?')[0];
    } else {
      return null;
    }
  }

  public setActiveUser(details?): void {
    this.globalEnvironment.setActiveUser(details);
  }

  public getActiveUser(): User {
    return this.globalEnvironment.getUserDetails();
  }

  public getSessionTimeoutInterval(): number{
   return this.globalEnvironment.getSessionMaxInactiveInterval();
  }

  public getUserMatrix(): UserMatrix{
    let activeUser = this.getActiveUser();
    let hasDesciberRole = false;
    let hasApproverRole = false;
    let isOrganizationSelected = false;
    let hasOrganizations = false;
    let isRiaMember = false;
    if (activeUser){
      hasApproverRole = -1 != activeUser.getRoles().indexOf('ROLE_HINDAJA');
      hasDesciberRole = -1 != activeUser.getRoles().indexOf('ROLE_KIRJELDAJA');
      isOrganizationSelected = activeUser.getActiveOrganization() != null;
      hasOrganizations = activeUser.getOrganizations().length > 0;
      if (activeUser.activeOrganization){
        isRiaMember = activeUser.activeOrganization.code == 70006317;
      }
    }

    return new UserMatrix({
      isLoggedIn: activeUser != null,
      hasApproverRole: hasApproverRole,
      hasDescriberRole: hasDesciberRole,
      isOrganizationSelected: isOrganizationSelected,
      isRiaMember: isRiaMember,
      hasOrganizations: hasOrganizations
    });
  }

  private runTrackingScripts(environment){
    let googleAnalyticsId = environment.getGoogleAnalyticsId();
    if (googleAnalyticsId){
      (function (i, s, o, g, r, a, m) {
        i['GoogleAnalyticsObject'] = r;
        i[r] = i[r] || function () {
          (i[r].q = i[r].q || []).push(arguments)
        }, i[r].l = 1 * <any>new Date();
        a = s.createElement(o),
          m = s.getElementsByTagName(o)[0];
        a.async = 1;
        a.src = g;
        m.parentNode.insertBefore(a, m)
      })(window, document, 'script', 'https://www.google-analytics.com/analytics.js', 'ga');

      ga('create',googleAnalyticsId, 'auto');
      ga('send', 'pageview');
    }

    let hjid = environment.getHotjarHjid();
    let hjsv = environment.getHotjarHjsv();
    if (hjid && hjsv){
      (function(h,o,t,j,a,r){
        h.hj=h.hj||function(){(h.hj.q=h.hj.q||[]).push(arguments)};
        h._hjSettings={hjid:hjid,hjsv:hjsv};
        a=o.getElementsByTagName('head')[0];
        r=o.createElement('script');r.async=1;
        r.src=t+h._hjSettings.hjid+j+h._hjSettings.hjsv;
        a.appendChild(r);
      })(<any>window,document,'https://static.hotjar.com/c/hotjar-','.js?sv=');
    }
  }

  public onAppStart(): Promise<any> {
    let promise = this.http.get(this.environmentUrl).toPromise();
    promise.then(response => {
      this.globalEnvironment = new Environment(response.json());
      this.runTrackingScripts(this.globalEnvironment);
    });
    return promise;
  }

  public loadEnvironmentData(){
    let promise = this.http.get(this.environmentUrl).toPromise();
    promise.then(res => {
      this.globalEnvironment = new Environment(res.json());
    });
    return promise;
  }

  public doLogout(): Promise<any> {
    return this.http.get('/logout').toPromise();
  }

  public doLogin(): Promise<any> {
    return this.http.get(this.userInfoUrl).toPromise();
  }

  public setActiveOrganization(organizationCode): Promise<any> {
    return this.http.put(this.environmentUrl + '/organization', organizationCode).toPromise();
  }

  constructor(private http: Http) {

  }

}
