import { User } from './user';

export class Environment {
  private userDetails: User;

  public getUserDetails(): User {
    return this.userDetails;
  }

  public setActiveUser(details?): void {
    if (details){
      this.userDetails = new User(details);
    } else {
      this.userDetails = null;
    }
  }

  constructor(options){
    this.userDetails = options.userDetails ? new User(options.userDetails) : null;
  }

}
