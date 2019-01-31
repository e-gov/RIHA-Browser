import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {System} from '../../../models/system';
import {ModalHelperService} from '../../../services/modal-helper.service';
import {ApproverAddIssueComponent} from '../../approver-add-issue/approver-add-issue.component';
import {ApproverIssueDetailsComponent} from '../../approver-issue-details/approver-issue-details.component';
import {SystemsService} from '../../../services/systems.service';
import {EnvironmentService} from '../../../services/environment.service';
import {UserMatrix} from '../../../models/user-matrix';
import {Location} from '@angular/common';
import {GeneralHelperService} from '../../../services/general-helper.service';
import _ from 'lodash';

@Component({
  selector: 'app-producer-details-issues',
  templateUrl: './producer-details-issues.component.html',
  styleUrls: ['./producer-details-issues.component.scss']
})
export class ProducerDetailsIssuesComponent implements OnInit {

  @Input() system: System;
  @Input() allowEdit: boolean;
  @Input() issueId: any;
  @Output() onIssueResolve = new  EventEmitter<string>();
  @Output() onIssueError = new  EventEmitter();

  comments: any[] = [];
  issues: any[] = [];
  activeIssues: any[] = [];
  closedIssues: any[] = [];
  newAdded: boolean = false;
  userMatrix: UserMatrix;
  hasStandardRelations: boolean = false;

  constructor(private modalService: ModalHelperService,
              private systemsService: SystemsService,
              private location: Location,
              private environmentService: EnvironmentService,
              private generalHelperService: GeneralHelperService) {
    this.userMatrix = this.environmentService.getUserMatrix();
  }

  openAddIssueModal(){
    const modalRef = this.modalService.open(ApproverAddIssueComponent, {
      size: "lg",
      backdrop: "static",
      keyboard: false
    });
    modalRef.componentInstance.system = this.system;
    modalRef.result.then(res => {
      this.refreshIssues();
      this.newAdded = true;
      setTimeout(()=> {
        this.newAdded = false;
      }, 5000)
    }, err => {});
  }

  openIssueDetailsModal(issueId){
    this.systemsService.getSystemIssueById(issueId).then(res => {
      this.location.replaceState(`/Infosüsteemid/Vaata/${ this.system.details.short_name }/Arutelu/${ issueId }`);
      const modalRef = this.modalService.open(ApproverIssueDetailsComponent,
        {
          size: "lg",
          backdrop: "static",
          windowClass: "fixed-header-modal",
          keyboard: false
        });
      modalRef.componentInstance.feedback = res.json();
      modalRef.componentInstance.system = this.system;
      modalRef.result.then(res => {
        this.refreshIssues();
        let issueType = res && res.issueType ? res.issueType : null;
        this.onIssueResolve.emit(issueType);
        this.location.replaceState(`/Infosüsteemid/Vaata/${ this.system.details.short_name }`);
      },
      err => {
        this.location.replaceState(`/Infosüsteemid/Vaata/${ this.system.details.short_name }`);
      });
    }, err => {
      this.onIssueError.emit(err);
    });
    return false;
  }

  refreshIssues(){
    this.systemsService.getSystemIssues(this.system.details.short_name).then(
      res => {
        this.issues = [];
        this.activeIssues = [];
        this.closedIssues = [];

        this.issues = res.json().content;
        this.activeIssues = this.issues.filter(i => i.status == 'OPEN');
        this.closedIssues = this.issues.filter(i => i.status == 'CLOSED');
      }
    )
  }

  canApprove(){
    this.userMatrix = this.environmentService.getUserMatrix();
    return this.allowEdit || this.userMatrix.hasApproverRole;
  }

  canRequestFeedback() {
    return !this.userMatrix.hasApproverRole && this.allowEdit && !this.system.hasUsedSystemTypeRelations && !this.generalHelperService.containsSpecialTopics(this.system);
  }

  canRequestFeedback() {

    const topicsWithNoFeedbackRequests = ["x-tee alamsüsteem", "standardlahendus", "asutusesiseseks kasutamiseks", "dokumendihaldussüsteem"];

    let topics = this.system.getTopics();

    if (!topics) {
      topics = [];
    }

    const lowerCasedTopics = _.map(topics, (topic) => topic ? topic.toLowerCase() : "");

    const foundForbiddenTopic = _.intersection(topicsWithNoFeedbackRequests, lowerCasedTopics).length > 0;

    // console.log('hasApproverRole: ', this.userMatrix.hasApproverRole, ", allowEdit: ", this.allowEdit,
    //   ", hasStandardRelations: ", this.hasStandardRelations, ", hasForbiddenTopics", foundForbiddenTopic);
    return !this.userMatrix.hasApproverRole && this.allowEdit && !this.hasStandardRelations && !foundForbiddenTopic;
  }

  ngOnInit() {
    this.refreshIssues();

    this.systemsService.getSystemRelations(this.system.details.short_name).then(res => {
      this.hasStandardRelations = res.json() != null
        && typeof _.find(res.json(), relation => relation != null && "USED_SYSTEM" === relation.type) !== 'undefined';
    });

    if (this.issueId){
      if (!this.userMatrix.isLoggedIn){
        //alert('please log in');
      } else if (!this.canApprove()){
        //alert('you cannot approve');
      } else {
        this.openIssueDetailsModal(this.issueId);
      }
    }
  }

}
