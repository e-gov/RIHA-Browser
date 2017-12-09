import { Component, OnInit, Input } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { EnvironmentService } from '../../services/environment.service';
import { ToastrService } from 'ngx-toastr';
import { User } from '../../models/user';
import { ModalHelperService } from "../../services/modal-helper.service";
import { G } from '../../globals/globals';
import { System } from '../../models/system';

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
  globals: any = G;

  refreshReplies(){
    this.systemService.getSystemIssueTimeline(this.feedback.id).then(
      res => {
        this.replies = res.json().content;
      });
  }

  markResolved(f){
    //comment can be empty
    this.systemService.closeSystemIssue(this.feedback.id, f.value).then(
      res => {
        this.refreshReplies();
        this.toastrService.success('Lahendatud');
        this.modalService.closeActiveModal();
      },
      err => {
        this.toastrService.error('Lahendatuks märkimine ebaõnnestus. Palun proovi uuesti.');
      }
    )
  }

  postReply(f){
    if (f.valid){
      this.systemService.postSystemIssueComment(this.feedback.id, f.value).then(
        res => {
          f.reset();
          this.refreshReplies();
          this.toastrService.success('Kommentaar edukalt lisatud.');
        },
        err => {
          this.toastrService.error('Kommentaari lisamine ebaõnnestus. Palun proovi uuesti.');
        }
      )
    }
  }

  getOrganizationWithUser(o){
    return `${o.organizationName} (${o.authorName})`;
  }

  canResolve(){
    let ret = false;
    if (this.feedback.status == 'OPEN'){
      let matrix = this.environmentService.getUserMatrix();
      if (this.feedback.type == this.globals.issue_type.TAKE_INTO_USE_REQUEST
        || this.feedback.type == this.globals.issue_type.MODIFICATION_REQUEST
        || this.feedback.type == this.globals.issue_type.FINALIZATION_REQUEST
        || this.feedback.type == this.globals.issue_type.ESTABLISHMENT_REQUEST){
          ret = matrix.hasApproverRole && matrix.isRiaMember;
      } else {
          ret = matrix.hasApproverRole || this.activeUser.canEdit(this.system.getOwnerCode());
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
              private environmentService: EnvironmentService) {
    this.activeUser = this.environmentService.getActiveUser();

  }

  ngOnInit() {
    this.refreshReplies();
  }

}
