import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Environment} from '../models/environment';
import {User} from '../models/user';
import {UserMatrix} from '../models/user-matrix';
import {environment} from '../../environments/environment';
import {Observable} from "rxjs";
import {System} from "../models/system";

declare const ga: Function;
export let classifiers: any;

@Injectable()
export class EnvironmentService {

  private environmentUrl = environment.api.environmentUrl;
  private userInfoUrl = environment.api.userInfoUrl;
  private classifiersUrl = environment.api.classifiersUrl;

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
    const activeUser = this.getActiveUser();
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
    const googleAnalyticsId = environment.getGoogleAnalyticsId();
    if (googleAnalyticsId){
      (function (i, s, o, g, r, a, m) {
        i['GoogleAnalyticsObject'] = r;
        i[r] = i[r] || function () {
          (i[r].q = i[r].q || []).push(arguments);
        }, i[r].l = 1 * <any>new Date();
        a = s.createElement(o),
          m = s.getElementsByTagName(o)[0];
        a.async = 1;
        a.src = g;
        m.parentNode.insertBefore(a, m);
      })(window, document, 'script', 'https://www.google-analytics.com/analytics.js', 'ga');

      ga('create', googleAnalyticsId, 'auto');
      ga('send', 'pageview');
    }

    const hjid = environment.getHotjarHjid();
    const hjsv = environment.getHotjarHjsv();
    if (hjid && hjsv){
      (function(h, o, t, j, a, r){
        h.hj = h.hj || function(){(h.hj.q = h.hj.q || []).push(arguments); };
        h._hjSettings = {hjid: hjid, hjsv: hjsv};
        a = o.getElementsByTagName('head')[0];
        r = o.createElement('script'); r.async = 1;
        r.src = t + h._hjSettings.hjid + j + h._hjSettings.hjsv;
        a.appendChild(r);
      })(<any>window, document, 'https://static.hotjar.com/c/hotjar-', '.js?sv=');
    }
  }

  public onAppStart(): Promise<any> {
    const observable = this.loadEnvironmentData().toPromise();
    observable.then(env => {
      this.runTrackingScripts(this.globalEnvironment);
    });
    return observable;
  }

  public loadClassifiers(): Promise<any> {
    const promise = this.http.get(this.classifiersUrl).toPromise();
    promise.then(response => {
      classifiers = Object.freeze(response);
    });

    return promise;
  }

  public loadEnvironmentData(): Observable<Environment> {
    const observable = this.http.get<Environment>(this.environmentUrl);
    observable.subscribe(environment => {
      console.log('loadEnvironmentData type', typeof environment, environment);
      this.globalEnvironment = new Environment(environment);
    });

    return observable;
  }

  public doLogout(): Observable<any> {
    return this.http.get('/logout');
  }

  public doLogin(): Observable<any> {
    return this.http.get(this.userInfoUrl);
  }

  public setActiveOrganization(organizationCode): Observable<Environment> {
    return this.http.put<Environment>(this.environmentUrl + '/organization', organizationCode);
  }

  constructor(private http: HttpClient) {

  }

}
