import {Injectable} from '@angular/core';
import {Headers, Http, RequestOptions, URLSearchParams} from '@angular/http';
import 'rxjs/add/operator/toPromise';
import {isNullOrUndefined} from 'util';
import {EnvironmentService} from './environment.service';
import {Observable} from "rxjs/Observable";

@Injectable()
export class SystemsService {

  private apiUrl = '/api/v1';
  private systemsUrl = this.apiUrl + '/systems';
  private issuesUrl =  this.apiUrl + '/issues';
  private myOrganizationUrl =  this.apiUrl + '/my/organization/users';

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

    return this.getSystems(filters, gridData, this.systemsUrl);
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
        if (filters.systemStatus == 'null'){
          filtersArr.push('meta.system_status.status,isnull,null');
        } else {
          filtersArr.push(`meta.system_status.status,jilike,${ filters.systemStatus }`);
        }
      }
      if (filters.developmentStatus){
        if (filters.developmentStatus == 'null'){
          filtersArr.push('meta.development_status,isnull,null');
        } else {
          filtersArr.push(`meta.development_status,jilike,${ filters.developmentStatus }`);
        }
      }
      if (filters.lastPositiveApprovalRequestType){
        if (filters.lastPositiveApprovalRequestType == 'null'){
          filtersArr.push('last_positive_approval_request_type,isnull,null');
        } else {
          filtersArr.push(`last_positive_approval_request_type,ilike,${ filters.lastPositiveApprovalRequestType }`);
        }
      }
      if (filters.xRoadStatus){
        if (filters.xRoadStatus == 'null'){
          filtersArr.push('meta.x_road_status.status,isnull,null');
        } else {
          filtersArr.push(`meta.x_road_status.status,jilike,${ filters.xRoadStatus }`);
        }
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

  public getSystemsObjectFiles(filters?, gridData?){
    let params: URLSearchParams = new URLSearchParams();

    if (!isNullOrUndefined(filters)) {
      params.append('filter', `data:Kommentaar:%${ filters.searchText }%`);
      params.append('filter', `data:Andmeobjekti nimi:%${ filters.searchText }%`);
    }

    if (!isNullOrUndefined(gridData.page)){
      params.set('page', gridData.page);
    }

    if (!isNullOrUndefined(gridData.sort)){
      params.set('sort', gridData.sort);
    }

    return this.http.get( this.systemsUrl +  '/files', {
      search: params
    }).toPromise();
  }

  public getSystemsDataObjects(filters?, gridData?){
    let params: URLSearchParams = new URLSearchParams();

    if (!isNullOrUndefined(filters)) {

      const possibleFilters = ['searchText', 'searchName', 'infosystem', 'dataObjectName', 'comment', 'parentObject', 'personalData'];

      let filterAtrributes = [];
      possibleFilters.forEach((possibleFilter) => {

      if (filters[possibleFilter]) {
        filterAtrributes.push(`${possibleFilter},ilike,%${ filters[possibleFilter]}%`);
      }});

      if (filterAtrributes.length > 0){
        params.set('filter', filterAtrributes.join());
      }
    }

    if (!isNullOrUndefined(gridData.page)){
      params.set('page', gridData.page);
    }

    if (!isNullOrUndefined(gridData.sort)){
      params.set('sort', gridData.sort);
    }

    return this.http.get( this.systemsUrl +  '/data-objects', {
      search: params
    }).toPromise();
  }

  public getSystem(reference) {
    return this.http.get(this.systemsUrl +  `/${ reference }`).toPromise();
  }

  public addSystem(value) {
    let system = {
      details: {
        short_name: value.short_name,
        name: value.name,
        purpose: value.purpose
      }
    };
    return this.http.post(this.systemsUrl, system).toPromise();
  }

  public postDataFile(file, reference){
    const formData = new FormData();
    formData.append('file', file);

    const headers = new Headers({});
    let options = new RequestOptions({ headers });

    return this.http.post(this.systemsUrl + `/${ reference }/files`, formData, options).toPromise();
  }

  public updateSystem(updatedData, reference?) {
    return this.http.put(this.systemsUrl + `/${ reference || updatedData.details.short_name }`, updatedData).toPromise();
  }

  public getSystemIssues(reference) {
    return this.http.get(this.systemsUrl + `/${ reference }/issues?size=1000&sort=-creation_date`).toPromise();
  }

  public addSystemIssue(reference, issue) {
    return this.http.post(this.systemsUrl + `/${ reference }/issues`, issue).toPromise();
  }

  public getSystemIssueById(issueId) {
    return this.http.get(this.issuesUrl + `/${ issueId }`).toPromise();
  }

  public getSystemIssueTimeline(issueId) {
    return this.http.get(this.issuesUrl + `/${ issueId }/timeline?size=1000`).toPromise();
  }

  public getActiveDiscussions(sort, relation?) {
    let params: URLSearchParams = new URLSearchParams();
    params.append('size', '1000');
    params.append('filter', 'status:OPEN');
    params.append('filter', 'sub_type');
    params.append('sort', sort ? sort : '-last_comment_creation_date');

    let urlToUse = '/api/v1/dashboard/issues';
    if (relation == 'person'){
      urlToUse += '/my';
    } else if (relation == 'organization'){
      urlToUse += '/org';
    }

    return this.http.get(urlToUse, {
      search: params
    }).toPromise();
  }

  public getActiveIssuesForOrganization(organizationCode, sort?) {
    let params: URLSearchParams = new URLSearchParams();
    params.append('size', '20');
    params.append('filter', 'status:OPEN');
    params.append('sort', sort ? sort : '-last_comment_creation_date');


    return this.http.get(`/api/v1/organizations/${ organizationCode }/systems/issues`, {
      search: params
    }).toPromise();
  }

  public getOpenApprovalRequests(sort) {
    let params: URLSearchParams = new URLSearchParams();

    params.set('filter', 'status,=,OPEN,sub_type,isnotnull,null');
    params.set('size', '1000');
    params.set('sort', sort);

    return this.http.get(this.issuesUrl, {
      search: params
    }).toPromise();
  }

  public getOrganizationUsers(gridData) {
    let params: URLSearchParams = new URLSearchParams();

    if (!isNullOrUndefined(gridData.page)){
      params.set('page', gridData.page);
    }
    if (!isNullOrUndefined(gridData.sort)){
      params.set('sort', gridData.sort);
    }

    return this.http.get(this.myOrganizationUrl, {
      params: params
    }).toPromise();
  }

  public postSystemIssueComment(issueId, reply) {
    return this.http.post(this.issuesUrl + `/${ issueId }/comments`, reply).toPromise();
  }

  public postSystemIssueDecision(issueId, decision) {
    return this.http.post(this.issuesUrl + `/${ issueId }/decisions`, decision).toPromise();
  }

  public getSystemRelations(reference) {
    return this.http.get(this.systemsUrl + `/${ reference }/relations`).toPromise();
  }

  public addSystemRelation(reference, relation) {
    return this.http.post(this.systemsUrl + `/${ reference }/relations`, relation).toPromise();
  }

  public createStandardRealisationSystem(reference, realisationModel) {
    return this.http.post(this.systemsUrl + `/${ reference }/create-standard-realisation-system`, realisationModel).toPromise();
  }

  public deleteSystemRelation(reference, relationId) {
    return this.http.delete(this.systemsUrl + `/${ reference }/relations/${ relationId }`).toPromise();
  }

  public closeSystemIssue(issueId, resolutionType) {
    return this.http.put(this.issuesUrl + `/${ issueId }`, {
      status: 'CLOSED',
      resolutionType: resolutionType || null
    }).toPromise();
  }

  constructor(private http: Http,
              private environmentService: EnvironmentService) { }

}
