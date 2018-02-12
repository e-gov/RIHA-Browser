import { Component, OnInit, Input } from '@angular/core';
import { G } from '../../../globals/globals';

@Component({
  selector: 'app-file-hint',
  templateUrl: './file-hint.component.html',
  styleUrls: ['./file-hint.component.scss']
})
export class FileHintComponent implements OnInit {

  @Input() file: any;
  globals: any = G;

  getOrganizationName(){
    if (this.file.accessRestriction && this.file.accessRestriction.organization){
      return this.file.accessRestriction.organization.name;
    } else {
      return '-';
    }
  }

  getRestrictionEndDate(){
    if (this.file.accessRestriction && this.file.accessRestriction.endDate){
      return this.file.accessRestriction.endDate;
    } else {
      return '-';
    }
  }

  getRestrictionStartDate(){
    if (this.file.accessRestriction && this.file.accessRestriction.startDate){
      return this.file.accessRestriction.startDate;
    } else {
      return '-';
    }
  }

  private getRestrictionReason(){
    if (this.file.accessRestriction && this.file.accessRestriction.reasonCode){
      let code = this.file.accessRestriction.reasonCode;
      let reason = this.globals.access_restriction_reasons.filter( r => r.code == this.file.accessRestriction.reasonCode)[0];
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

  constructor() {
  }

  ngOnInit() {
  }

}
