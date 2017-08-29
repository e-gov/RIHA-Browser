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

  constructor(options?){
    this.personalCode = options.personalCode || null;
    this.firstName = options.firstName || null;
    this.lastName = options.lastName || null;
    this.activeOrganizationId = options.activeOrganizationId || null;
    this.organizations = options.organizations || [];
  }
}
