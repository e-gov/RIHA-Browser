import { Component, OnInit, Input } from '@angular/core';
import { System } from '../../../models/system';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
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

  comments: any[] = [];
  activeIssues: any[] = [];
  closedIssues: any[] = [];
  newAdded: boolean = false;
  userMatrix: UserMatrix;

  openAddIssueModal(){
    const modalRef = this.modalService.open(ApproverAddIssueComponent);
    this.system.details.legislations = this.system.details.legislations || [];
    modalRef.componentInstance.system = this.system;
    modalRef.result.then(res => {
      this.refreshIssues();
      this.newAdded = true;
      setTimeout(()=> {
        this.newAdded = false;
      }, 5000)
    }, err => {});
  }

  openIssueDetailsModal(comment){
    this.systemsService.getSystemIssueById(comment.id).then(res => {
      const modalRef = this.modalService.open(ApproverIssueDetailsComponent);
      modalRef.componentInstance.feedback = res.json();
      modalRef.result.then(res => {
        this.refreshIssues();
      },
      err => {

      });
    });
    return false;
  }

  refreshIssues(){
    this.systemsService.getSystemIssues(this.system.details.short_name).then(
      res => {
        this.activeIssues = [];
        this.closedIssues = [];

        res.json().content.map(c => {
          if (c.status === 'OPEN'){
            this.activeIssues.push(c);
          } else if (c.status === 'CLOSED'){
            this.closedIssues.push(c);
          }
        });
      }
    )
  };

  constructor(private modalService: NgbModal,
              private systemsService: SystemsService,
              private environmentService: EnvironmentService) {
    this.userMatrix = this.environmentService.getUserMatrix();
  }

  ngOnInit() {
    this.refreshIssues();
  }

}
