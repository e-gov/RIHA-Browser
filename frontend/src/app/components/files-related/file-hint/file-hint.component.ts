import { Component, OnInit, Input } from '@angular/core';
import {classifiers} from "../../../services/environment.service";
import {GeneralHelperService} from "../../../services/general-helper.service";

@Component({
  selector: 'app-file-hint',
  templateUrl: './file-hint.component.html',
  styleUrls: ['./file-hint.component.scss']
})
export class FileHintComponent implements OnInit {

  @Input() file: any;
  classifiers = classifiers;

  getOrganizationName(){
    if (this.file.accessRestriction && this.file.accessRestriction.organization){
      return this.file.accessRestriction.organization.name;
    } else {
      return '-';
    }
  }

  getRestrictionEndDate(){
    if (this.file.accessRestriction && this.file.accessRestriction.endDate){
      if (this.isDateObject(this.file.accessRestriction.endDate)) {
        return this.generalHelperService.dateObjToTimestamp(this.file.accessRestriction.endDate, true);
      } else {
        return this.file.accessRestriction.endDate;
      }
    } else {
      return '-';
    }
  }

  getRestrictionStartDate(){
    if (this.file.accessRestriction && this.file.accessRestriction.startDate){
      if (this.isDateObject(this.file.accessRestriction.startDate)) {
        return this.generalHelperService.dateObjToTimestamp(this.file.accessRestriction.startDate, true);
      } else {
        return this.file.accessRestriction.startDate;
      }
    } else {
      return '-';
    }
  }

  private getRestrictionReason(){
    if (this.file.accessRestriction && this.file.accessRestriction.reasonCode){
      let code = this.file.accessRestriction.reasonCode;
      let reason = this.classifiers.access_restriction_reasons.filter( r => r.code == this.file.accessRestriction.reasonCode)[0];
      return reason;
    } else {
      return null;
    }
  }

  getReasonLegislation(){
    return this.getRestrictionReason().legislation;
  }

  getReasonDescription(){
    return this.getRestrictionReason().description;
  }

  private isDateObject(date: any): boolean {
    return date.day || date.month || date.year;
  }

  constructor(private generalHelperService: GeneralHelperService) {
  }

  ngOnInit() {
  }

}
