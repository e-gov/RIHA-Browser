export class User {
  personalCode: string;
  firstName: string;
  lastName: string;
  activeOrganization: any;
  organizations: any[];
  roles: string[];

  public getOrganizations(): any[]{
    return this.organizations;
  }

  public getActiveOrganization(): any{
    return this.activeOrganization;
  }

  public getFullName(): string {
    return `${this.firstName} ${this.lastName}`;
  }

  public getRoles(): string[] {
    return this.roles;
  }

  public hasApproverRole(): boolean{
    return -1 != this.getRoles().indexOf('ROLE_HINDAJA');
  }

  public canEdit(organizationCode): boolean {
    const ao = this.getActiveOrganization();
    if (ao != null && ao.code == organizationCode){
      return this.roles.indexOf('ROLE_KIRJELDAJA') != -1;
    } else {
      return false;
    }
  }

  public getFullNameWithActiveOrganization(): string {
    let ret = `${this.firstName} ${this.lastName}`;

    if (this.activeOrganization){
        ret += ` (${ this.activeOrganization.name })`
    }
    return ret;
  }

  constructor(options?){
    this.personalCode = options.personalCode || null;
    this.firstName = options.firstName || null;
    this.lastName = options.lastName || null;
    this.activeOrganization = options.activeOrganization || null;
    this.organizations = options.organizations || [];
    this.roles = options.roles || [];
  }
}
