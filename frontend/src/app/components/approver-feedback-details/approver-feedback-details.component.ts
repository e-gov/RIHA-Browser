import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-approver-feedback-details',
  templateUrl: './approver-feedback-details.component.html',
  styleUrls: ['./approver-feedback-details.component.scss']
})
export class ApproverFeedbackDetailsComponent implements OnInit {

  @Input() comment: any;

  constructor() {

  }

  ngOnInit() {
  }

}
