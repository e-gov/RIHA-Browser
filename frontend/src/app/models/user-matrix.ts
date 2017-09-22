export class UserMatrix {
  isLoggedIn: boolean;
  hasApproverRole: boolean;
  hasDescriberRole: boolean;
  isOrganizationSelected: boolean;
  hasOrganizations: boolean;

  constructor(options){
    this.isLoggedIn = options.isLoggedIn === true;
    this.hasApproverRole = options.hasApproverRole === true;
    this.hasDescriberRole = options.hasDescriberRole === true;
    this.isOrganizationSelected = options.isOrganizationSelected === true;
    this.hasOrganizations = options.hasOrganizations === true;
  }
}
