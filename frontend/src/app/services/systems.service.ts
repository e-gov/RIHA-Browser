import {Injectable} from '@angular/core';
import {EnvironmentService} from './environment.service';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {HttpClient, HttpParams} from '@angular/common/http';
import {System} from "../models/system";
import {SystemIssue} from "../models/system-issue";
import {SystemRelation} from "../models/system-relation";
import {environment} from "../../environments/environment";

@Injectable()
export class SystemsService {

  private systemsUrl = environment.api.systemsUrl;
  private issuesUrl =  environment.api.issuesUrl;
  private myOrganizationUrl =  environment.api.myOrganizationUrl;

  public dateObjToTimestamp(dateObj: any, simple?: boolean): any {
    if ((dateObj) && dateObj.year && dateObj.month && dateObj.day) {
      const year = dateObj.year.toString();
      let month = dateObj.month.toString();
      let day = dateObj.day.toString();

      if (month.length === 1) {month = '0' + month}
      if (day.length === 1) {day = '0' + day}
      if (simple === true) {
        return `${ year }-${ month }-${ day }`;
      } else {
        return `${ year }-${ month }-${ day }T00:00:00Z`;
      }
    } else {
      return dateObj;
    }
  }

  public timestampToDateObj(timestamp: string): any {
    if (timestamp != null || timestamp !== undefined && timestamp.substr && timestamp !== '') {
      const year = parseInt(timestamp.substr(0, 4), 10);
      const month = parseInt(timestamp.substr(5, 2), 10);
      const day = parseInt(timestamp.substr(8, 2), 10);
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
    if (system.details.meta.approval_status && system.details.meta.approval_status.timestamp) {
      system.details.meta.approval_status.timestamp = this.timestampToDateObj(system.details.meta.approval_status.timestamp);
    }
    if (system.details.meta.x_road_status && system.details.meta.x_road_status.timestamp) {
      system.details.meta.x_road_status.timestamp = this.timestampToDateObj(system.details.meta.x_road_status.timestamp);
    }
    if (system.details.meta.system_status && system.details.meta.system_status.timestamp) {
      system.details.meta.system_status.timestamp = this.timestampToDateObj(system.details.meta.system_status.timestamp);
    }
    return system;
  }

  public prepareSystemForSending(system: any) {
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

  public getAlertText(errObj): string {
    let ret = null;
    const code = errObj.code || errObj.error.code;
    if (code === 'validation.system.shortNameAlreadyTaken') {
      ret = 'Lühinimi on juba kasutusel';
    } else {
      ret = errObj.message;
    }
    return ret;
  }

  public getOwnSystems(filters?, gridData?) {
    filters = filters || {};

    const user = this.environmentService.getActiveUser();
    if (user && user.getActiveOrganization()) {
      filters.ownerCode = user.getActiveOrganization().code;
    }

    return this.getSystems(filters, gridData, this.systemsUrl);
  }

  public getSystems(filters?, gridData?, url?): Observable<any> {

    const filtersArr: string[] = [];
    let params: HttpParams = new HttpParams();

    if (filters !== null) {
      if (filters.searchText) {
        filtersArr.push(`search_content,ilike,%${ filters.searchText }%`);
      }
      if (filters.name) {
        filtersArr.push(`name,ilike,%${ filters.name }%`);
      }
      if (filters.shortName) {
        filtersArr.push(`short_name,ilike,%${ filters.shortName }%`);
      }
      if (filters.ownerCode) {
        filtersArr.push(`owner.code,jilike,%${ filters.ownerCode }%`);
      }
      if (filters.ownerName) {
        filtersArr.push(`owner.name,jilike,%${ filters.ownerName }%`);
      }
      if (filters.purpose) {
        filtersArr.push(`purpose,jilike,%${ filters.purpose }%`);
      }
      if (filters.topic) {
        filtersArr.push(`topics,jarr,%${ filters.topic }%`);
      }
      if (filters.storedData) {
        filtersArr.push(`stored_data,jarr,%${ filters.storedData }%`);
      }
      if (filters.systemStatus) {
        if (filters.systemStatus === 'null') {
          filtersArr.push('meta.system_status.status,isnull,null');
        } else {
          filtersArr.push(`meta.system_status.status,jilike,${ filters.systemStatus }`);
        }
      }
      if (filters.developmentStatus) {
        if (filters.developmentStatus === 'null') {
          filtersArr.push('meta.development_status,isnull,null');
        } else {
          filtersArr.push(`meta.development_status,jilike,${ filters.developmentStatus }`);
        }
      }
      if (filters.lastPositiveApprovalRequestType) {
        if (filters.lastPositiveApprovalRequestType === 'null') {
          filtersArr.push('last_positive_approval_request_type,isnull,null');
        } else {
          filtersArr.push(`last_positive_approval_request_type,ilike,${ filters.lastPositiveApprovalRequestType }`);
        }
      }
      if (filters.xRoadStatus) {
        if (filters.xRoadStatus === 'null') {
          filtersArr.push('meta.x_road_status.status,isnull,null');
        } else {
          filtersArr.push(`meta.x_road_status.status,jilike,${ filters.xRoadStatus }`);
        }
      }
      if (filters.dateCreatedFrom) {
        filtersArr.push(`j_creation_timestamp,>,${ filters.dateCreatedFrom }`);
      }
      if (filters.dateCreatedTo) {
        filtersArr.push(`j_creation_timestamp,<,${ filters.dateCreatedTo }T23:59:59`);
      }
      if (filters.dateUpdatedFrom) {
        filtersArr.push(`j_update_timestamp,>,${ filters.dateUpdatedFrom }`);
      }
      if (filters.dateUpdatedTo) {
        filtersArr.push(`j_update_timestamp,<,${ filters.dateUpdatedTo }T23:59:59`);
      }

      if (filtersArr.length > 0) {
        params = params.set('filter', filtersArr.join());
      }
    }

    if (gridData.page !== null) {
      params = params.set('page', gridData.page);
    }

    if (gridData.sort !== null) {
      params = params.set('sort', gridData.sort);
    }

    const urlToUse = url || this.systemsUrl;
    return this.http.get(urlToUse, {
      params: params
    });
  }

  public getSystemsForAutocomplete(text, ownShortName?, url?): Observable<any> {
    let params: HttpParams = new HttpParams();
    params = params.set('searchTerm', text);
    const urlToUse = url || (this.systemsUrl + "/autocomplete");

    return this.http.get<any>(urlToUse, {
      params: params
    })
      .pipe(map((response: any) => <any>response.content))
  }

  public getSystemsObjectFiles(filters?, gridData?): Observable<any> {
    let params: HttpParams = new HttpParams();

    if (filters !== null) {
      params.append('filter', `data:Kommentaar:%${ filters.searchText }%`);
      params.append('filter', `data:Andmeobjekti nimi:%${ filters.searchText }%`);
    }

    if (gridData.page !== null) {
      params = params.set('page', gridData.page);
    }

    if (gridData.sort != null || gridData.sort !== undefined) {
      params = params.set('sort', gridData.sort);
    }

    return this.http.get( this.systemsUrl +  '/files', {
      params: params
    });
  }

  public getSystemsDataObjects(filters?, gridData?): Observable<any> {
    let params: HttpParams = new HttpParams();

    if (filters != null || filters !== undefined) {

      const possibleFilters = ['searchText', 'searchName', 'infosystem', 'dataObjectName', 'comment', 'parentObject', 'personalData'];

      const filterAtrributes = [];
      possibleFilters.forEach((possibleFilter) => {

      if (filters[possibleFilter]) {
        filterAtrributes.push(`${possibleFilter},ilike,%${ filters[possibleFilter]}%`);
      }});

      if (filterAtrributes.length > 0) {
        params = params.set('filter', filterAtrributes.join());
      }
    }

    if (gridData.page != null || gridData.page !== undefined) {
      params = params.set('page', gridData.page);
    }

    if (gridData.sort != null || gridData.sort !== undefined) {
      params = params.set('sort', gridData.sort);
    }

    return this.http.get( this.systemsUrl +  '/data-objects', {
      params: params
    });
  }

  public getSystem(reference): Observable<System> {
    return this.http.get<System>(this.systemsUrl +  `/${ reference }`);
  }

  public addSystem(value): Observable<System> {
    const system = {
      details: {
        short_name: value.short_name,
        name: value.name,
        purpose: value.purpose
      }
    };
    return this.http.post<System>(this.systemsUrl, system);
  }

  public postDataFile(file, reference): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post(this.systemsUrl + `/${ reference }/files`, formData, { responseType: 'text' });
  }

  public updateSystem(updatedData, reference?): Observable<System> {
    return this.http.put<System>(this.systemsUrl + `/${ reference || updatedData.details.short_name }`, updatedData);
  }

  public getSystemIssues(reference): Observable<any> {
    return this.http.get(this.systemsUrl + `/${ reference }/issues?size=1000&sort=-creation_date`);
  }

  public addSystemIssue(reference, issue): Observable<SystemIssue> {
    return this.http.post<SystemIssue>(this.systemsUrl + `/${ reference }/issues`, issue);
  }

  public getSystemIssueById(issueId): Observable<SystemIssue> {
    return this.http.get<SystemIssue>(this.issuesUrl + `/${ issueId }`);
  }

  public getSystemIssueTimeline(issueId): Observable<any> {
    return this.http.get<any>(this.issuesUrl + `/${ issueId }/timeline?size=1000`);
  }

  public getActiveDiscussions(sort, relation?): Observable<any> {
    let params: HttpParams = new HttpParams();
    params = params.append('size', '1000');
    params = params.append('filter', 'status:OPEN');
    params = params.append('filter', 'sub_type');
    params = params.append('sort', sort ? sort : '-last_comment_creation_date');

    let urlToUse = '/api/v1/dashboard/issues';
    if (relation === 'person') {
      urlToUse += '/my';
    } else if (relation === 'organization') {
      urlToUse += '/org';
    }

    return this.http.get(urlToUse, {
      params: params
    });
  }

  public getActiveIssuesForOrganization(organizationCode, gridData?): Observable<any> {
    let params: HttpParams = new HttpParams();

    params = params.append('filter', 'status:OPEN');
    if (gridData) {
      if (gridData.sort) {
        params = params.set('sort', gridData.sort);
      } else {
        params = params.set('sort', '-last_comment_creation_date');
      }
      params = params.set('page', gridData.page);
    }

    return this.http.get(`/api/v1/organizations/${ organizationCode }/systems/issues`, {
      params: params
    });
  }

  public getOpenApprovalRequests(sort): Observable<any> {
    let params: HttpParams = new HttpParams();

    params = params.set('filter', 'status,=,OPEN,sub_type,isnotnull,null');
    params = params.set('size', '1000');
    params = params.set('sort', sort);

    return this.http.get(this.issuesUrl, {
      params: params
    });
  }

  public getOrganizationUsers(gridData): Observable<any> {
    let params: HttpParams = new HttpParams();

    if (gridData.page != null || gridData.page !== undefined) {
      params = params.set('page', gridData.page);
    }
    if (gridData.sort != null || gridData.sort !== undefined) {
      params = params.set('sort', gridData.sort);
    }

    return this.http.get(this.myOrganizationUrl, {
      params: params
    });
  }

  public postSystemIssueComment(issueId, reply): Observable<any> {
    return this.http.post(this.issuesUrl + `/${ issueId }/comments`, reply);
  }

  public postSystemIssueDecision(issueId, decision): Observable<any> {
    return this.http.post(this.issuesUrl + `/${ issueId }/decisions`, decision);
  }

  public getSystemRelations(reference): Observable<SystemRelation[]> {
    return this.http.get<SystemRelation[]>(this.systemsUrl + `/${ reference }/relations`);
  }

  public addSystemRelation(reference, relation): Observable<any> {
    return this.http.post(this.systemsUrl + `/${ reference }/relations`, relation);
  }

  public createStandardRealisationSystem(reference, realisationModel): Observable<System> {
    return this.http.post<System>(this.systemsUrl + `/${ reference }/create-standard-realisation-system`, realisationModel);
  }

  public deleteSystemRelation(reference, relationId): Observable<any> {
    return this.http.delete(this.systemsUrl + `/${ reference }/relations/${ relationId }`);
  }

  public closeSystemIssue(issueId, resolutionType): Observable<any> {
    return this.http.put(this.issuesUrl + `/${ issueId }`, {
      status: 'CLOSED',
      resolutionType: resolutionType || null
    });
  }

  constructor(private http: HttpClient,
              private environmentService: EnvironmentService) { }

}
