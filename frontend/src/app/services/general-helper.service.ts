import {Injectable} from '@angular/core';
import {globals} from "./environment.service";
import {isNullOrUndefined} from 'util';
import {Title} from '@angular/platform-browser';
import {Location} from '@angular/common';
import {Router} from '@angular/router';
import {WindowRefService} from '../services/window-ref.service';
import {ToastrService} from 'ngx-toastr';
import {System} from '../models/system';
import _ from 'lodash';

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

  public isMenuActive(blockId, first?){
    let element = $(`#${blockId}`)[0];
    if (element){
      let yOffset = $(element).offset().top - $(document).scrollTop();
      let height = element.offsetHeight;
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
      let status = system.details.meta.system_status.status;
      switch (status) {
        case globals.system_status.IN_USE: {
          statusDescription = 'kasutusel';
          break;
        }
        case globals.system_status.ESTABLISHING: {
          statusDescription = 'asutamisel';
          break;
        }
        case globals.system_status.FINISHED: {
          statusDescription = 'lõpetatud';
          break
        }
      }
    }
    return statusDescription;
  }

  public getApprovalStatusText(system : System){
    let statusDescription = 'kooskõlastamata';

    if (system.hasUsedSystemTypeRelations || this.containsSpecialTopics(system)) {
      statusDescription = 'registreeritud';
    }

    if (system.lastPositiveApprovalRequestType) {
      let status = system.lastPositiveApprovalRequestType;
      switch (status) {
        case globals.issue_type.TAKE_INTO_USE_REQUEST: {
          statusDescription = 'kasutamine kooskõlastatud';
          break;
        }
        case globals.issue_type.ESTABLISHMENT_REQUEST: {
          statusDescription = 'asutamine kooskõlastatud';
          break;
        }
        case globals.issue_type.FINALIZATION_REQUEST: {
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

  public scrollTo(el){
    $('html, body').animate({
      scrollTop: $(el).offset().top
    }, 500);
    this.location.replaceState(this.router.url.split('#')[0] + el)
  }

  public adjustSection(hash?){
    hash = hash || this.winRef.nativeWindow.location.hash;
    if (hash){
      let elId = decodeURI(hash.replace('#',''));
      let el = $(hash)[0];
      if (el){
        $('html, body').animate({
          scrollTop: $(el).offset().top
        }, 500);
      }
    }
  }

  public containsSpecialTopics(system: System): boolean {
    let topics = system.getTopics();

    if (!topics) {
      topics = [];
    }

    const lowerCasedTopics = _.map(topics, (topic) => topic ? topic.toLowerCase() : "");

    return _.intersection(globals.topics_that_do_not_require_feedback_on_creation, lowerCasedTopics).length > 0;
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
