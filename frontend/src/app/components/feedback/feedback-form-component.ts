import {Component, OnInit} from '@angular/core';
import {UserMatrix} from "../../models/user-matrix";
import {EnvironmentService} from "../../services/environment.service";
import {SystemFeedback} from "../../models/system-feedback";
import {SystemFeedbackService} from "../../services/system-feedback.service";
import {ActivatedRoute} from '@angular/router';
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'feedback-form',
  templateUrl: './feedback-form-component.html',
  styleUrls: ['./feedback-form-component.scss']
})
export class FeedbackFormComponent implements OnInit {

  systemFeedback: SystemFeedback;
  isRedirectedLogout: boolean;
  grades;
  submissionError: boolean = false;
  isSubmitted: boolean = false;
  isError = false;
  userMatrix: UserMatrix;

  constructor(private environmentService: EnvironmentService,
              private systemFeedbackService: SystemFeedbackService,
              private route: ActivatedRoute,
              private toastrService: ToastrService) {
    this.systemFeedback = new SystemFeedback()
    this.grades = Array(11).fill(0).map((x, i) => i);
    this.userMatrix = this.environmentService.getUserMatrix()
  }

  ngOnInit() {
    this.isRedirectedLogout = this.route.snapshot.params.logout != null;
  }

  submitFeedbackForm() {
    if (this.validateInput()) {
      this.submissionError = true;
      return;
    }
    this.isError = false;

    this.systemFeedbackService.leaveFeedback(this.systemFeedback).subscribe((res => {
        this.isSubmitted = true;
      }),
      error => {
        this.isSubmitted = true;
        this.isError = true;
      });
  }


  validateInput() {
    return this.systemFeedback.grade === '';
  }

  getNotificationHeader() {
    return this.isError
      ? 'Serveri viga! Proovige uuesti!'
      : 'Täname tagasiside eest!';
  }

  getNotificationClass() {
    return this.isError ? 'danger' : 'success';
  }

}
