export class User {
  personalCode: string;
  firstName: string;
  lastName: string;
  activeOrganizationId: number;
  organizations: any[];

  public getOrganizations(): any[]{
    return this.organizations;
  }

  public getFullName(): string {
    return `${this.firstName} ${this.lastName}`;
  }

  public getFullNameWithActiveOrganization(): string {
    let ret = `${this.firstName} ${this.lastName}`;

    if (this.activeOrganizationId){
      let activeOrganization = this.organizations.filter(o => {return o.id == this.activeOrganizationId})[0];
      if (activeOrganization){
        ret += ` (${ activeOrganization.name })`
      }
    }
    return ret;
  }

  constructor(options?){
    this.personalCode = options.personalCode || null;
    this.firstName = options.firstName || null;
    this.lastName = options.lastName || null;
    this.activeOrganizationId = options.activeOrganizationId || null;
    this.organizations = options.organizations || [];
  }
}
