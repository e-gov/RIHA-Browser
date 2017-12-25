import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { System } from '../../../models/system';
import { ModalHelperService } from '../../../services/modal-helper.service';
import { ApproverAddIssueComponent } from '../../approver-add-issue/approver-add-issue.component';
import { ApproverIssueDetailsComponent } from '../../approver-issue-details/approver-issue-details.component';
import { SystemsService } from '../../../services/systems.service';
import { EnvironmentService } from '../../../services/environment.service';
import { UserMatrix } from '../../../models/user-matrix';

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
      const modalRef = this.modalService.open(ApproverIssueDetailsComponent,
        {
          size: "lg",
          backdrop: "static",
          keyboard: false
        });
      modalRef.componentInstance.feedback = res.json();
      modalRef.componentInstance.system = this.system;
      modalRef.result.then(res => {
        this.refreshIssues();
        let issueType = res && res.issueType ? res.issueType : null;
        this.onIssueResolve.emit(issueType);
      },
      err => {

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
    return this.allowEdit || this.userMatrix.hasApproverRole;
  }

  constructor(private modalService: ModalHelperService,
              private systemsService: SystemsService,
              private environmentService: EnvironmentService) {
    this.userMatrix = this.environmentService.getUserMatrix();
  }

  ngOnInit() {
    this.refreshIssues();
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
