import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-approver-add-comment',
  templateUrl: './approver-add-comment.component.html',
  styleUrls: ['./approver-add-comment.component.scss']
})
export class ApproverAddCommentComponent implements OnInit {

  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit() {
  }

}
