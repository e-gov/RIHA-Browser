import { User } from './user';

export class Environment {
  private userDetails: User;
  private tracking: any;

  public getGoogleAnalyticsId(){
    let ret = null;
    if (this.tracking && this.tracking.googleAnalytics && this.tracking.googleAnalytics.id){
      ret = this.tracking.googleAnalytics.id;
    }
    return ret;
  }

  public getHotjarHjid(){
    let ret = null;
    if (this.tracking && this.tracking.hotjar && this.tracking.hotjar.hjid){
      ret = this.tracking.hotjar.hjid;
    }
    return ret;
  }

  public getHotjarHjsv(){
    let ret = null;
    if (this.tracking && this.tracking.hotjar && this.tracking.hotjar.hjsv){
      ret = this.tracking.hotjar.hjsv;
    }
    return ret;
  }

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
    this.tracking = options.tracking ? options.tracking : {
      googleAnalytics: {
        id: null
      },
      hotjar: {
        hjid: null,
        hjsv: null
      }
    }
  }

}
