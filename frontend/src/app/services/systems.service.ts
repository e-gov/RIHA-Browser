import { Injectable } from '@angular/core';
import { Http, URLSearchParams  } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { isNullOrUndefined } from 'util';

@Injectable()
export class SystemsService {

  private systemsUrl = '/systems';

  public getOwnSystems(filters?, gridData?){
    filters = filters || {};
    //set current user as owner
    //filters.owner = '';

    return this.getSystems(filters, gridData);
  }

  public getSystems(filters?, gridData?) {

    let params: URLSearchParams = new URLSearchParams();
    let filtersArr: string[] = [];

    if (!isNullOrUndefined(filters)){
      if (filters.name){
        filtersArr.push(`name,ilike,%${ filters.name }%`);
      }
      if (filters.shortName){
        filtersArr.push(`short_name,ilike,%${ filters.shortName }%`);
      }
      if (filters.owner){
        filtersArr.push(`owner,ilike,%${ filters.owner }%`);
      }
      if (filtersArr.length > 0){
        params.set('filter', filtersArr.join());
      }
    }

    if (!isNullOrUndefined(gridData.page)){
      params.set('page', gridData.page);
    }

    if (!isNullOrUndefined(gridData.sort)){
      params.set('sort', gridData.sort);
    }

    return this.http.get(this.systemsUrl, {
      search: params
    }).toPromise();
  }

  public addSystem(value) {
    let system = {
      details: {
        short_name: value.short_name,
        name: value.name,
        purpose: value.purpose
      }
    }
    return this.http.post(this.systemsUrl, system).toPromise();
  }

  constructor(private http: Http) { }

}
