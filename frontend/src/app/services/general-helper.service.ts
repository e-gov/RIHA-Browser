import { Injectable } from '@angular/core';
import { G } from '../globals/globals';
import { isNullOrUndefined } from 'util';
import { Title } from '@angular/platform-browser';

declare var $: any;

@Injectable()
export class GeneralHelperService {

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

  public getSystemStatusText(system){
    let statusDescription = 'määramata';
    if (system.details.meta && system.details.meta.system_status) {
      let status = system.details.meta.system_status.status;
      switch (status) {
        case G.system_status.IN_USE: {
          statusDescription = 'kasutusel';
          break;
        }
        case G.system_status.ESTABLISHING: {
          statusDescription = 'asutamisel';
          break;
        }
        case G.system_status.FINISHED: {
          statusDescription = 'lõpetatud';
          break
        }
      }
    }
    return statusDescription;
  }

  public getApprovalStatusText(system){
    let statusDescription = 'määramata';
    if (system.lastPositiveApprovalRequestType) {
      let status = system.lastPositiveApprovalRequestType;
      switch (status) {
        case G.issue_type.TAKE_INTO_USE_REQUEST: {
          statusDescription = 'kasutamine kooskõlastatud';
          break;
        }
        case G.issue_type.ESTABLISHMENT_REQUEST: {
          statusDescription = 'asutamine kooskõlastatud';
          break;
        }
        case G.issue_type.FINALIZATION_REQUEST: {
          statusDescription = 'lõpetamine kooskõlastatud';
          break
        }
      }
    }
    return statusDescription;
  }

  public generateQueryString(obj){
    let newObj = {};
    Object.keys(obj).forEach(function(key) {
      if (obj[key]){
        newObj[key] = obj[key];
      }
    });
    return $.param(newObj);
  }

  public getFileUrl(url, reference){
    if (url.substring(0,7) === 'file://'){
      return `/api/v1/systems/${ reference }/files/${ url.substring(7) }`;
    } else {
      return url;
    }
  }

  public setRihaPageTitle(titleTxt?){
    if (titleTxt) {
      this.title.setTitle(`${ titleTxt } - Riigi infosüsteemi haldussüsteem RIHA`);
    } else {
      this.title.setTitle(`Riigi infosüsteemi haldussüsteem RIHA`);
    }
  }

  public truncateString(str, length){
    return str.length > length ? str.substring(0, length - 3) + '...' : str;
  }

  public cloneObject(obj){
    return $.extend(true, {}, obj);
  }

  constructor(private title: Title) { }

}
