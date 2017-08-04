import { Component, OnInit, Input } from '@angular/core';
import { System } from '../../../models/system';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ApproverAddCommentComponent } from '../../approver-add-comment/approver-add-comment.component';
import { SystemsService } from '../../../services/systems.service';

@Component({
  selector: 'app-producer-details-comments',
  templateUrl: './producer-details-comments.component.html',
  styleUrls: ['./producer-details-comments.component.scss']
})
export class ProducerDetailsCommentsComponent implements OnInit {

  @Input() system: System;

  comments: any[] = [];
  activeComments: any[] = [];
  closedComments: any[] = [];

  openAddCommentModal(){
    const modalRef = this.modalService.open(ApproverAddCommentComponent);
    this.system.details.legislations = this.system.details.legislations || [];
    modalRef.componentInstance.system = this.system;
    modalRef.result.then(res => {
      this.refreshComments();
    });
  }

  showDiscussion(){
    return false;
  }

  refreshComments(){
    this.activeComments = [];
    this.closedComments = [];
    this.systemsService.getSystemComments(this.system.details.uuid).then(
      res => {
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
    this.refreshComments();
  }

}
