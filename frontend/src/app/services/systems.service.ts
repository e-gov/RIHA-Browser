import { Injectable } from '@angular/core';
import { Http, URLSearchParams  } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { isNullOrUndefined } from 'util';
import { EnvironmentService } from './environment.service';
import * as moment from 'moment';

@Injectable()
export class SystemsService {

  private systemsUrl = '/systems';

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

  public prepareSystemForDisplay(system: any){
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

  public getOwnSystems(filters?, gridData?){
    filters = filters || {};

    return this.getSystems(filters, gridData, `${ this.environmentService.getProducerUrl() }/systems`);
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

    let urlToUse = url || this.systemsUrl;

    return this.http.get(urlToUse, {
      search: params
    }).toPromise();
  }

  public getSystem(short_name) {
    return this.http.get(`${ this.environmentService.getProducerUrl() }/systems/${ short_name }`).toPromise();
  }

  public addSystem(value) {
    let system = {
      details: {
        short_name: value.short_name,
        name: value.name,
        purpose: value.purpose
      }
    }
    return this.http.post(`${ this.environmentService.getProducerUrl() }/systems`, system).toPromise();
  }

  public updateSystem(updatedData, shortName?) {
    return this.http.put(`${ this.environmentService.getProducerUrl() }/systems/${ shortName || updatedData.details.short_name }`, updatedData).toPromise();
  }

  public getSystemIssues(uuid) {
    return this.http.get(`${ this.environmentService.getApproverUrl() }/systems/${ uuid }/issues?size=1000`).toPromise();
  }

  public addSystemIssue(uuid, issue) {
    return this.http.post(`${ this.environmentService.getApproverUrl() }/systems/${ uuid }/issues`, issue).toPromise();
  }

  public getSystemIssueById(issueId) {
    return this.http.get(`${ this.environmentService.getApproverUrl() }/issues/${ issueId }`).toPromise();
  }

  public getSystemIssueTimeline(uuid, commentId) {
    return this.http.get(`${ this.environmentService.getApproverUrl() }/issues/${ commentId }/timeline`).toPromise();
  }

  public postSystemIssueComment(issueId, reply) {
    return this.http.post(`${ this.environmentService.getApproverUrl() }/issues/${ issueId }/comments`, reply).toPromise();
  }

  public closeSystemIssue(issueId, reply) {
    return this.http.put(`${ this.environmentService.getApproverUrl() }/issues/${ issueId }`, {
      comment: reply.comment,
      status: 'CLOSED'
    }).toPromise();
  }

  constructor(private http: Http,
              private environmentService: EnvironmentService) { }

}
