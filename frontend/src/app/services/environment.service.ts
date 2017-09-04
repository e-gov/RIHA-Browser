import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Environment } from '../models/environment';
import { User } from '../models/user';

@Injectable()
export class EnvironmentService {

  public globalEnvironment: any;

  public getProducerUrl(): string {
    return this.globalEnvironment.remotes.producerUrl;
  }

  public getApproverUrl(): string {
    return this.globalEnvironment.remotes.approverUrl;
  }

  public setActiveUser(details?): void {
    this.globalEnvironment.setActiveUser(details);
  }

  public getActiveUser(): User {
    return this.globalEnvironment.getUserDetails();
  };

  load(): Promise<any> {
    let promise = this.http.get('/environment').toPromise();
    promise.then(response => this.globalEnvironment = new Environment(response.json()));
    return promise;
  }

  constructor(private http: Http) {

  }

}
