import { Component, OnInit, Input } from '@angular/core';
import { System } from '../../../models/system';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ApproverAddCommentComponent } from '../../approver-add-comment/approver-add-comment.component';

@Component({
  selector: 'app-producer-details-comments',
  templateUrl: './producer-details-comments.component.html',
  styleUrls: ['./producer-details-comments.component.scss']
})
export class ProducerDetailsCommentsComponent implements OnInit {

  @Input() system: System;

  openAddCommentModal(){
    const modalRef = this.modalService.open(ApproverAddCommentComponent);
    this.system.details.legislations = this.system.details.legislations || [];
    modalRef.componentInstance.system = this.system;
  }

  constructor(private modalService: NgbModal) { }

  ngOnInit() {
  }

}
