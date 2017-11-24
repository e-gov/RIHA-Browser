import { Component, OnInit, Input } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { EnvironmentService } from '../../services/environment.service';
import { System } from '../../models/system';
import { ToastrService } from 'ngx-toastr';
import { User } from '../../models/user';
import { ModalHelperService } from '../../services/modal-helper.service';
import { G } from '../../globals/globals';

@Component({
  selector: 'app-approver-add-comment',
  templateUrl: './approver-add-issue.component.html',
  styleUrls: ['./approver-add-issue.component.scss']
})
export class ApproverAddIssueComponent implements OnInit {

  @Input() system: System;
  activeUser: User;
  globals: any = G;
  isApprovalRequest: boolean;
  approvalRequest: any = {
    title: null,
    comment: null,
    type: null
  };

  switchView(){
    this.isApprovalRequest = !this.isApprovalRequest;
    this.approvalRequest = {
      title: null,
      comment: null,
      type: null
    };
  }

  onSubmitNewIssue(f) :void {
    if (f.valid){
      this.systemsService.addSystemIssue(this.system.details.short_name, f.value).then(
        res => {
          this.modalService.closeActiveModal();
        },
        err => {
          this.toastrService.error('Serveri viga! Proovige uuesti!');
        })
    }
  }

  onSubmitApprovalRequest(f) :void {
    if (f.valid) {
      this.systemsService.addSystemIssue(this.system.details.short_name, this.approvalRequest).then(
        res => {
          this.modalService.closeActiveModal();
        },
        err => {
          this.toastrService.error('Serveri viga! Proovige uuesti!');
        });
    }
  }

  onIssueTypeChange(issueType){
    switch (issueType){
      case this.globals.issue_type.ESTABLISHMENT_REQUEST: {
        this.approvalRequest.title = 'Infosüsteemil puudub asutamise kooskõlastus';
        break;
      }
      case this.globals.issue_type.FINALIZATION_REQUEST: {
        this.approvalRequest.title = 'Infosüsteemil puudub lõpetamise kooskõlastus';
        break;
      }
      case this.globals.issue_type.MODIFICATION_REQUEST: {
        this.approvalRequest.title = 'Infosüsteemil puudub andmekoosseisu muutmise kooskõlastus';
        break;
      }
      case this.globals.issue_type.TAKE_INTO_USE_REQUEST: {
        this.approvalRequest.title = 'Infosüsteemil puudub kasutusele võtmise kooskõlastus';
        break;
      }
    }
  }

  closeModal(f){
    if (f.form.dirty){
      if (confirm('Oled sisestanud väljadesse infot. Kui navigeerid siit ära ilma salvestamata, siis sinu sisestatud info kaob.')){
        this.modalService.dismissActiveModal();
      } else {
        return false;
      }
    } else {
      this.modalService.dismissActiveModal();
    }
  }

  canSwitchViews(){
    return this.environmentService.getUserMatrix().hasApproverRole && this.activeUser.canEdit(this.system.getOwnerCode());
  }

  constructor(private modalService: ModalHelperService,
              private systemsService: SystemsService,
              private toastrService: ToastrService,
              private environmentService: EnvironmentService) {
  }

  ngOnInit() {
    this.activeUser = this.environmentService.getActiveUser();
    this.isApprovalRequest = !this.activeUser.hasApproverRole();
  }

}
