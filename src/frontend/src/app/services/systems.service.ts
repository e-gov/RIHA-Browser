import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';

@Injectable()
export class SystemsService {

  private systemsUrl = '/systems';

  public getSystems(): any {
    return this.http.get(this.systemsUrl).toPromise();
  }

  constructor(private http: Http) { }

}
