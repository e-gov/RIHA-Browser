import { Injectable } from '@angular/core';
import { Http, URLSearchParams  } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { isNullOrUndefined } from 'util';

@Injectable()
export class SystemsService {

  private systemsUrl = '/systems';

  public getSystems(filters?, page?): any {

    let params: URLSearchParams = new URLSearchParams();
    let filtersArr: string[] = [];

    if (!isNullOrUndefined(filters)){
      if (filters.name){
        filtersArr.push(`name,ilike,%${ filters.name }%`);
      }
      if (filters.owner){
        filtersArr.push(`owner,ilike,%${ filters.owner }%`);
      }
      if (filtersArr.length > 0){
        params.set('filter', filtersArr.join());
      }
    }

    if (!isNullOrUndefined(page)){
      params.set('page', page);
    }

    return this.http.get(this.systemsUrl, {
      search: params
    }).toPromise();
  }

  constructor(private http: Http) { }

}
