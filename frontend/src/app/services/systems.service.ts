import { Injectable } from '@angular/core';
import { Http, URLSearchParams, Headers, RequestOptions  } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { isNullOrUndefined } from 'util';
import { EnvironmentService } from './environment.service';
import * as moment from 'moment';
import {Observable} from "rxjs/Observable";

@Injectable()
export class SystemsService {

  private systemsUrl = '/api/v1/systems';

  public dateObjToTimestamp(dateObj: any, simple?: boolean): any {
    if (!isNullOrUndefined(dateObj) && dateObj.year && dateObj.month && dateObj.day){
      let year = dateObj.year.toString();
      let month = dateObj.month.toString();
      let day = dateObj.day.toString();

      if (month.length === 1) month = '0' + month;
      if (day.length === 1) day = '0' + day;
      if (simple === true){
        return `${ year }-${ month }-${ day }`;
      } else {
        return `${ year }-${ month }-${ day }T00:00:00Z`;
      }
    } else {
      return dateObj;
    }
  }

  public timestampToDateObj(timestamp: string): any {
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
    if (code === 'validation.system.shortNameAlreadyTaken'){
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
      if (filters.searchText){
        filtersArr.push(`search_content,ilike,%${ filters.searchText }%`);
      }
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
      if (filters.purpose){
        filtersArr.push(`purpose,jilike,%${ filters.purpose }%`);
      }
      if (filters.topic){
        filtersArr.push(`topics,jarr,%${ filters.topic }%`);
      }
      if (filters.storedData){
        filtersArr.push(`stored_data,jarr,%${ filters.storedData }%`);
      }
      if (filters.systemStatus){
        filtersArr.push(`meta.system_status.status,jilike,${ filters.systemStatus }`);
      }
      if (filters.developmentStatus){
        filtersArr.push(`meta.development_status,jilike,${ filters.developmentStatus }`);
      }
      if (filters.lastPositiveApprovalRequestType){
        filtersArr.push(`last_positive_approval_request_type,ilike,${ filters.lastPositiveApprovalRequestType }`);
      }
      if (filters.xRoadStatus){
        filtersArr.push(`meta.x_road_status.status,jilike,${ filters.xRoadStatus }`);
      }
      if (filters.dateCreatedFrom){
        filtersArr.push(`j_creation_timestamp,>,${ filters.dateCreatedFrom }`);
      }
      if (filters.dateCreatedTo){
        filtersArr.push(`j_creation_timestamp,<,${ filters.dateCreatedTo }T23:59:59`);
      }
      if (filters.dateUpdatedFrom){
        filtersArr.push(`j_update_timestamp,>,${ filters.dateUpdatedFrom }`);
      }
      if (filters.dateUpdatedTo){
        filtersArr.push(`j_update_timestamp,<,${ filters.dateUpdatedTo }T23:59:59`);
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

  public getSystemsForAutocomplete(text, ownShortName?, url?): Observable<any> {
    let params: URLSearchParams = new URLSearchParams();
    let filtersArr: string[] = [];

    filtersArr.push(`name,ilike,%${ text }%`);

    params.set('filter', filtersArr.join());

    params.set('size', '10');

    let urlToUse = url || this.systemsUrl;

    return this.http.get(urlToUse, {
      search: params
    }).map(response => {
      return <any>response.json().content;
    });
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
    return this.http.get(`/api/v1/systems/${ shortName }/issues?size=1000&sort=-creation_date`).toPromise();
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

  public getSystemRelations(shortName) {
    return this.http.get(`/api/v1/systems/${ shortName }/relations`).toPromise();
  }

  public addSystemRelation(shortName, relation) {
    return this.http.post(`/api/v1/systems/${ shortName }/relations`, relation).toPromise();
  }

  public deleteSystemRelation(shortName, relationId) {
    return this.http.delete(`/api/v1/systems/${ shortName }/relations/${ relationId }`).toPromise();
  }

  public closeSystemIssue(issueId, reply) {
    return this.http.put(`/api/v1/issues/${ issueId }`, {
      comment: reply.comment,
      status: 'CLOSED',
      resolutionType: reply.resolutionType || null
    }).toPromise();
  }

  constructor(private http: Http,
              private environmentService: EnvironmentService) { }

}
