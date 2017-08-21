import { Component, OnInit, Input } from '@angular/core';
import { System } from '../../../models/system';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ApproverAddIssueComponent } from '../../approver-add-issue/approver-add-issue.component';
import { ApproverIssueDetailsComponent } from '../../approver-issue-details/approver-issue-details.component';
import { SystemsService } from '../../../services/systems.service';

@Component({
  selector: 'app-producer-details-comments',
  templateUrl: './producer-details-issues.component.html',
  styleUrls: ['./producer-details-issues.component.scss']
})
export class ProducerDetailsIssuesComponent implements OnInit {

  @Input() system: System;

  comments: any[] = [];
  activeComments: any[] = [];
  closedComments: any[] = [];
  newAdded: boolean = false;

  openAddCommentModal(){
    const modalRef = this.modalService.open(ApproverAddIssueComponent);
    this.system.details.legislations = this.system.details.legislations || [];
    modalRef.componentInstance.system = this.system;
    modalRef.result.then(res => {
      this.refreshIssues();
      this.newAdded = true;
      setTimeout(()=> {
        this.newAdded = false;
      }, 5000)
    });
  }

  openFeedbackDetailsModal(comment){
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
    this.systemsService.getSystemIssues(this.system.details.uuid).then(
      res => {
        this.activeComments = [];
        this.closedComments = [];
        
        res.json().content.map(c => {
          if (c.status === 'OPEN'){
            this.activeComments.push(c);
          } else if (c.status === 'CLOSED'){
            this.closedComments.push(c);
          }
        });
      }
    )
  };

  constructor(private modalService: NgbModal,
              private systemsService: SystemsService) { }

  ngOnInit() {
    this.refreshIssues();
  }

}
