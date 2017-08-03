import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

@Injectable()
export class EnvironmentService {

  public globalEnvironment: any;

  public getProducerUrl(): string {
    return this.globalEnvironment.remotes.producerUrl;
  }

  public getApproverUrl(): string {
    return this.globalEnvironment.remotes.approverUrl;
  }

  load(): Promise<any> {
    let promise = this.http.get('/environment').toPromise();
    promise.then(response => this.globalEnvironment = response.json());
    return promise;
  }

  constructor(private http: Http) {

  }

}
