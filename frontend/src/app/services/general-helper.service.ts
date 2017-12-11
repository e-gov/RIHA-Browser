import { Injectable } from '@angular/core';
import { G } from '../globals/globals';

declare var $: any;

@Injectable()
export class GeneralHelperService {

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

  public generateQueryString(obj){
    let newObj = {};
    Object.keys(obj).forEach(function(key) {
      if (obj[key]){
        newObj[key] = obj[key];
      }
    });
    return $.param(newObj);
  }

  public getFileUrl(url){
    if (url.substring(0,7) === 'file://'){
      return '/api/v1/files/' + url.substring(7);
    } else {
      return url;
    }
  }

  public truncateString(str, length){
    return str.length > length ? str.substring(0, length - 3) + '...' : str;
  }

  public cloneObject(obj){
    return $.extend(true, {}, obj);
  }

  constructor() { }

}
