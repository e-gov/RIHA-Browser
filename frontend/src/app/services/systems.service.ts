import { Injectable } from '@angular/core';
import { Http, URLSearchParams, Headers, RequestOptions  } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { isNullOrUndefined } from 'util';
import { EnvironmentService } from './environment.service';
import * as moment from 'moment';

@Injectable()
export class SystemsService {

  private systemsUrl = '/api/v1/systems';

  private dateObjToTimestamp(dateObj: any): any {
    if (!isNullOrUndefined(dateObj) && dateObj.year && dateObj.month && dateObj.day){
      let year = dateObj.year.toString();
      let month = dateObj.month.toString();
      let day = dateObj.day.toString();

      if (month.length === 1) month = '0' + month;
      if (day.length === 1) day = '0' + day;
      return `${ year }-${ month }-${ day }T00:00:00Z`;
    } else {
      return dateObj;
    }
  }

  private timestampToDateObj(timestamp: string): any {
    if (!isNullOrUndefined(timestamp) && timestamp.substr && timestamp != ''){
      let year = parseInt(timestamp.substr(0, 4), 10);
      let month = parseInt(timestamp.substr(5, 2), 10);
      let day = parseInt(timestamp.substr(8, 2), 10);
      return {
        year: year,
        month: month,
        day: day
      };
    } else {
      return timestamp;
    }
  }

  public prepareSystemForDisplay(system: any): any {
    if (system.details.meta.approval_status && system.details.meta.approval_status.timestamp){
      system.details.meta.approval_status.timestamp = this.timestampToDateObj(system.details.meta.approval_status.timestamp);
    }
    if (system.details.meta.x_road_status && system.details.meta.x_road_status.timestamp){
      system.details.meta.x_road_status.timestamp = this.timestampToDateObj(system.details.meta.x_road_status.timestamp);
    }
    if (system.details.meta.system_status && system.details.meta.system_status.timestamp){
      system.details.meta.system_status.timestamp = this.timestampToDateObj(system.details.meta.system_status.timestamp);
    }
    return system;
  }

  public prepareSystemForSending(system: any){
    if (system.details.meta.approval_status && system.details.meta.approval_status.timestamp) {
      system.details.meta.approval_status.timestamp = this.dateObjToTimestamp(system.details.meta.approval_status.timestamp);
    }
    if (system.details.meta.x_road_status && system.details.meta.x_road_status.timestamp) {
      system.details.meta.x_road_status.timestamp = this.dateObjToTimestamp(system.details.meta.x_road_status.timestamp);
    }
    if (system.details.meta.system_status && system.details.meta.system_status.timestamp) {
      system.details.meta.system_status.timestamp = this.dateObjToTimestamp(system.details.meta.system_status.timestamp);
    }
    return system;
  }

  public getAlertText(errObj): string{
    let ret = null;
    let code = errObj.code;
    if (code === 'validation.system.shortName.alreadyTaken'){
      ret = 'Lühinimi on juba kasutusel';
    } else {
      ret = errObj.message;
    }
    return ret;
  }

  public getOwnSystems(filters?, gridData?){
    filters = filters || {};

    let user = this.environmentService.getActiveUser();
    if (user && user.getActiveOrganization()){
      filters.ownerCode = user.getActiveOrganization().code;
    }

    return this.getSystems(filters, gridData, `/api/v1/systems`);
  }

  public getSystems(filters?, gridData?, url?) {

    let params: URLSearchParams = new URLSearchParams();
    let filtersArr: string[] = [];

    if (!isNullOrUndefined(filters)){
      if (filters.name){
        filtersArr.push(`name,ilike,%${ filters.name }%`);
      }
      if (filters.shortName){
        filtersArr.push(`short_name,ilike,%${ filters.shortName }%`);
      }
      if (filters.ownerCode){
        filtersArr.push(`owner.code,jilike,%${ filters.ownerCode }%`);
      }
      if (filters.ownerName){
        filtersArr.push(`owner.name,jilike,%${ filters.ownerName }%`);
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

    let urlToUse = url || this.systemsUrl;

    return this.http.get(urlToUse, {
      search: params
    }).toPromise();
  }

  public getSystem(short_name) {
    return this.http.get(`/api/v1/systems/${ short_name }`).toPromise();
  }

  public addSystem(value) {
    let system = {
      details: {
        short_name: value.short_name,
        name: value.name,
        purpose: value.purpose
      }
    }
    return this.http.post(`/api/v1/systems`, system).toPromise();
  }

  public postDataFile(file){
    const formData = new FormData();
    formData.append('file', file);

    const headers = new Headers({});
    let options = new RequestOptions({ headers });

    return this.http.post(`/api/v1/files`, formData, options).toPromise();
  }

  public updateSystem(updatedData, shortName?) {
    return this.http.put(`/api/v1/systems/${ shortName || updatedData.details.short_name }`, updatedData).toPromise();
  }

  public getSystemIssues(shortName) {
    return this.http.get(`/api/v1/systems/${ shortName }/issues?size=1000`).toPromise();
  }

  public addSystemIssue(shortName, issue) {
    return this.http.post(`/api/v1/systems/${ shortName }/issues`, issue).toPromise();
  }

  public getSystemIssueById(issueId) {
    return this.http.get(`/api/v1/issues/${ issueId }`).toPromise();
  }

  public getSystemIssueTimeline(issueId) {
    return this.http.get(`/api/v1/issues/${ issueId }/timeline`).toPromise();
  }

  public postSystemIssueComment(issueId, reply) {
    return this.http.post(`/api/v1/issues/${ issueId }/comments`, reply).toPromise();
  }

  public closeSystemIssue(issueId, reply) {
    return this.http.put(`/api/v1/issues/${ issueId }`, {
      comment: reply.comment,
      status: 'CLOSED'
    }).toPromise();
  }

  constructor(private http: Http,
              private environmentService: EnvironmentService) { }

}
