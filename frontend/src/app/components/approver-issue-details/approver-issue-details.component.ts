import { Component, OnInit, Input } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { EnvironmentService } from '../../services/environment.service';
import { ToastrService } from 'ngx-toastr';
import { User } from '../../models/user';
import { ModalHelperService } from "../../services/modal-helper.service";
import { classifiers } from "../../services/environment.service";
import { System } from '../../models/system';
import { GeneralHelperService } from '../../services/general-helper.service';

@Component({
  selector: 'app-approver-feedback-details',
  templateUrl: './approver-issue-details.component.html',
  styleUrls: ['./approver-issue-details.component.scss']
})
export class ApproverIssueDetailsComponent implements OnInit {

  @Input() feedback: any;
  @Input() system: System;

  replies: any[] = [];
  activeUser: User;
  classifiers = classifiers;
  decisionType: string = 'null';
  commentText: string = '';
  deadlinePassed: boolean;

  refreshReplies(){
    this.systemService.getSystemIssueTimeline(this.feedback.id).then(
      res => {
        this.replies = res.json().content;
      });
  }

  resetValues(f){
    f.resetForm();
    this.decisionType = 'null';
    this.commentText = '';
  }

  markResolved(resolutionType?){
    this.systemService.closeSystemIssue(this.feedback.id, resolutionType).then(
      res => {
        this.refreshReplies();
        this.toastrService.success('Lahendatud');
        this.modalService.closeActiveModal({issueType: resolutionType});
      },
      err => {
        this.toastrService.error('Lahendatuks märkimine ebaõnnestus. Palun proovi uuesti.');
      }
    )
  }

  markResolvedWithVerdict(resolutionType){
    this.markResolved(resolutionType);
  }

  postComment(f){
    if (f.valid && this.commentText){
      this.systemService.postSystemIssueComment(this.feedback.id, {
        comment: this.commentText
      }).then(
        res => {
          this.resetValues(f);
          this.refreshReplies();
          this.toastrService.success('Kommentaar edukalt lisatud.');
        },
        err => {
          this.resetValues(f);
          this.toastrService.error('Kommentaari lisamine ebaõnnestus. Palun proovi uuesti.');
        }
      )
    }
  }

  postDecision(f){
    if (f.valid){
      this.systemService.postSystemIssueDecision(this.feedback.id, {
        comment: this.commentText,
        decisionType: this.decisionType
      }).then(
        res => {
          this.resetValues(f);
          this.refreshReplies();
          this.toastrService.success('Otsus edukalt lisatud.');
        },
        err => {
          this.resetValues(f);
          this.toastrService.error('Otsuse lisamine ebaõnnestus. Palun proovi uuesti.');
        }
      )
    }
  }

  getOrganizationWithUser(o){
    return `${o.organizationName} (${o.authorName})`;
  }

  canPostDecision(){
    return this.feedback.type && this.environmentService.getUserMatrix().hasApproverRole;
  }

  canResolveGeneral(){
    let ret = false;
    if (this.feedback.status == 'OPEN'){
      let bHasApproverRole = this.environmentService.getUserMatrix().hasApproverRole;
      if (this.feedback.type != classifiers.issue_type.TAKE_INTO_USE_REQUEST.code
        && this.feedback.type != classifiers.issue_type.MODIFICATION_REQUEST.code
        && this.feedback.type != classifiers.issue_type.FINALIZATION_REQUEST.code
        && this.feedback.type != classifiers.issue_type.ESTABLISHMENT_REQUEST.code){
        ret = bHasApproverRole || this.activeUser.canEdit(this.system.getOwnerCode());
      }
    }
    return ret;
  }
  canResolveWithVerdict(){
    let ret = false;
    if (this.feedback.status == 'OPEN'){
      if (this.feedback.type == classifiers.issue_type.TAKE_INTO_USE_REQUEST.code
        || this.feedback.type == classifiers.issue_type.MODIFICATION_REQUEST.code
        || this.feedback.type == classifiers.issue_type.FINALIZATION_REQUEST.code
        || this.feedback.type == classifiers.issue_type.ESTABLISHMENT_REQUEST.code){
          let userMatrix = this.environmentService.getUserMatrix();
          ret = userMatrix.hasApproverRole && userMatrix.isRiaMember;
      }
    }
    return ret;
  }

  closeModal(f){
    if (f.form.dirty){
      if (confirm('Oled sisestanud väljadesse infot. Kui navigeerid siit ära ilma salvestamata, siis sinu sisestatud info kaob.')){
        this.modalService.closeActiveModal();
      } else {
        return false;
      }
    } else {
      this.modalService.closeActiveModal();
    }
  }

  constructor(private systemService: SystemsService,
              private toastrService: ToastrService,
              private modalService: ModalHelperService,
              private generalHelperService: GeneralHelperService,
              private environmentService: EnvironmentService) {
    this.activeUser = this.environmentService.getActiveUser();

  }

  ngOnInit() {
    this.deadlinePassed = Date.now() > Date.parse(this.feedback.decisionDeadline);
    this.refreshReplies();
  }

}
