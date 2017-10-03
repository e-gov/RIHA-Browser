import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Environment } from '../models/environment';
import { User } from '../models/user';
import { UserMatrix } from '../models/user-matrix';

@Injectable()
export class EnvironmentService {

  private environmentUrl = '/api/v1/environment';

  public globalEnvironment: any;

  public setActiveUser(details?): void {
    this.globalEnvironment.setActiveUser(details);
  }

  public getActiveUser(): User {
    return this.globalEnvironment.getUserDetails();
  };

  public getUserMatrix(): UserMatrix{
    let activeUser = this.getActiveUser();
    let hasDesciberRole = false;
    let hasApproverRole = false;
    let isOrganizationSelected = false;
    let hasOrganizations = false;
    if (activeUser){
      hasApproverRole = -1 != activeUser.getRoles().indexOf('ROLE_HINDAJA');
      hasDesciberRole = -1 != activeUser.getRoles().indexOf('ROLE_KIRJELDAJA');
      isOrganizationSelected = activeUser.getActiveOrganization() != null;
      hasOrganizations = activeUser.getOrganizations().length > 0
    }

    return new UserMatrix({
      isLoggedIn: activeUser != null,
      hasApproverRole: hasApproverRole,
      hasDescriberRole: hasDesciberRole,
      isOrganizationSelected: isOrganizationSelected,
      hasOrganizations: hasOrganizations
    });
  }

  public load(): Promise<any> {
    let promise = this.http.get(this.environmentUrl).toPromise();
    promise.then(response => this.globalEnvironment = new Environment(response.json()));
    return promise;
  }

  public doLogout(): Promise<any> {
    return this.http.get('/logout').toPromise();
  }

  public doLogin(): Promise<any> {
    return this.http.get('/login/esteid').toPromise();
  }

  public setActiveOrganization(organizationCode): Promise<any> {
    return this.http.put(this.environmentUrl + '/organization', organizationCode).toPromise();
  }

  constructor(private http: Http) {

  }

}
