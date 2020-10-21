import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserMatrix} from "../../models/user-matrix";
import {EnvironmentService} from "../../services/environment.service";
import {GeneralHelperService} from '../../services/general-helper.service';
import {SystemFeedback} from "../../models/system-feedback";
import {SystemFeedbackService} from "../../services/system-feedback.service";
import {ActivatedRoute} from '@angular/router';
import {ToastrService} from "ngx-toastr";
import {ReCaptchaV3Service} from 'ng-recaptcha';
import {makeRecaptchaBadgeInvisible, makeRecaptchaBadgeVisible} from '../../utils/commonUtils';


@Component({
  selector: 'feedback-form',
  templateUrl: './feedback-form-component.html',
  styleUrls: ['./feedback-form-component.scss']
})
export class FeedbackFormComponent implements OnInit, OnDestroy {

  systemFeedback: SystemFeedback;
  isRedirectedLogout: boolean;
  grades;
  submissionError: boolean = false;
  isSubmitted: boolean = false;
  isError = false;
  userMatrix: UserMatrix;
  recaptchaProperties: any;
  recaptchaBadgeStyle: HTMLStyleElement;

  constructor(private environmentService: EnvironmentService,
              private generalHelperService: GeneralHelperService,
              private systemFeedbackService: SystemFeedbackService,
              private route: ActivatedRoute,
              private recaptchaV3Service: ReCaptchaV3Service,
              private toastrService: ToastrService) {
    this.systemFeedback = new SystemFeedback()
    this.grades = Array(11).fill(0).map((x, i) => i);
    this.userMatrix = this.environmentService.getUserMatrix()
  }

  ngOnDestroy(): void {
    makeRecaptchaBadgeInvisible(this.recaptchaBadgeStyle);
  }

  ngOnInit() {
    this.generalHelperService.setRihaPageTitle('Tagasiside');
    this.isRedirectedLogout = this.route.snapshot.params.logout != null;
    this.recaptchaProperties = this.environmentService.getRecaptchaProperties();
    this.recaptchaBadgeStyle = makeRecaptchaBadgeVisible();
  }

  submitFeedbackForm() {
    if (this.validateInput()) {
      this.submissionError = true;
      return;
    }
    this.isError = false;

    try {
      if (this.recaptchaProperties.enabled) {
        this.recaptchaV3Service.execute('feedbackAction')
          .subscribe((token) => {
            this.sendFeedbackToServer(token);
          }, error => {
            this.isSubmitted = true;
            this.isError = true;
          });
      } else {
        this.sendFeedbackToServer();
      }
    } catch (e) {
      console.log(e);
      this.isSubmitted = true;
      this.isError = true;
    }
  }


  private sendFeedbackToServer(token?: string) {
    this.systemFeedback.recaptchaToken = token;
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
