export class User {
  personalCode: string;
  firstName: string;
  lastName: string;
  activeOrganization: any;
  organizations: any[];

  public getOrganizations(): any[]{
    return this.organizations;
  }

  public getActiveOrganization(): any{
    return this.activeOrganization;
  }

  public getFullName(): string {
    return `${this.firstName} ${this.lastName}`;
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
  }
}
