import {Injectable} from '@angular/core';
import {classifiers} from "./environment.service";
import {isNullOrUndefined} from 'util';
import {Title} from '@angular/platform-browser';
import {Location} from '@angular/common';
import {Router} from '@angular/router';
import {WindowRefService} from '../services/window-ref.service';
import {ToastrService} from 'ngx-toastr';
import {System} from '../models/system';
import _ from 'lodash';
import * as $ from 'jquery';

@Injectable()
export class GeneralHelperService {

  public dateObjToTimestamp(dateObj: any, simple?: boolean): any {
    if (!isNullOrUndefined(dateObj) && dateObj.year && dateObj.month && dateObj.day){
      const year = dateObj.year.toString();
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

  public isMenuActive(blockId, first?){
    const element = $(`#${blockId}`)[0];
    if (element){
      const yOffset = $(element).offset().top - $(document).scrollTop();
      const height = element.offsetHeight;
      if (first === true) {
        return yOffset + height > 0;
      } else {
        return yOffset <= 0 && (yOffset + height > 0)
      }
    } else {
      return false
    }
  }

  public getSystemStatusText(system){
    let statusDescription = 'määramata';
    if (system.details.meta && system.details.meta.system_status) {
      const status = system.details.meta.system_status.status;
      switch (status) {
        case classifiers.system_status.IN_USE.code: {
          statusDescription = 'kasutusel';
          break;
        }
        case classifiers.system_status.ESTABLISHING.code: {
          statusDescription = 'asutamisel';
          break;
        }
        case classifiers.system_status.FINISHED.code: {
          statusDescription = 'lõpetatud';
          break
        }
      }
    }
    return statusDescription;
  }

  public getApprovalStatusText(system : System){
    let statusDescription = 'kooskõlastamata';

    if (system.lastPositiveApprovalRequestType) {
      const status = system.lastPositiveApprovalRequestType;
      switch (status) {
        case classifiers.issue_type.AUTOMATICALLY_REGISTERED.code: {
          statusDescription = 'registreeritud';
          break;
        }
        case classifiers.issue_type.TAKE_INTO_USE_REQUEST.code: {
          statusDescription = 'kasutamine kooskõlastatud';
          break;
        }
        case classifiers.issue_type.ESTABLISHMENT_REQUEST.code: {
          statusDescription = 'asutamine kooskõlastatud';
          break;
        }
        case classifiers.issue_type.FINALIZATION_REQUEST.code: {
          statusDescription = 'lõpetamine kooskõlastatud';
          break
        }
      }
    }
    return statusDescription;
  }

  public generateQueryString(obj){
    const newObj = {};
    Object.keys(obj).forEach(function(key) {
      if (obj[key]){
        newObj[key] = obj[key];
      }
    });
    return $.param(newObj);
  }

  public toArray(obj) {
    return Object.keys(obj).map((key) => {
      return {code: obj[key].code, value: obj[key].value}
    });
  }

  public compareClassifiers(c1, c2): boolean {
    return (!c1 && !c2) || (c1 && c2 && c1 === c2);
  }

  public toJsonArray(obj) {
    return Object.keys(obj).map((key) => {
      return {code: obj[key].code, value: JSON.parse(obj[key].value)}
    });
  }

  public toArrayOfValues(obj) {
    return Object.keys(obj).map((key) => {
      return obj[key].value
    });
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

  public scrollTo(el){
    $('html, body').animate({
      scrollTop: $(el).offset().top
    }, 500);
    this.location.replaceState(this.location.path().split('#')[0] + el)
  }

  public adjustSection(hash?){
    hash = hash || this.winRef.nativeWindow.location.hash;
    if (hash){
      const el = $(hash)[0];
      if (el){
        $('html, body').animate({
          scrollTop: $(el).offset().top
        }, 500);
      }
    }
  }

  public containsSpecialTopics(system: System, treatStandardLahendusAsSpecialTopic = true): boolean {
    let topics = system.getTopics();

    if (!topics) {
      topics = [];
    }

    const lowerCasedTopics = _.map(topics, (topic) => topic ? topic.toLowerCase() : "");
    if (!treatStandardLahendusAsSpecialTopic) {
      _.remove(lowerCasedTopics, (topic) => "standardlahendus" == topic);
    }

    return _.intersection(this.toArrayOfValues(classifiers.topics_that_do_not_require_feedback_on_creation), lowerCasedTopics).length > 0;
  }

  public showError(txt?){
    this.toastrService.error(txt ? txt : 'Serveri viga')
  }

  constructor(private title: Title,
              private winRef: WindowRefService,
              private location: Location,
              private toastrService: ToastrService,
              private router: Router) { }

}
