import { Component, OnInit, Input } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { EnvironmentService, classifiers } from '../../services/environment.service';
import { System } from '../../models/system';
import { ToastrService } from 'ngx-toastr';
import { User } from '../../models/user';
import { ModalHelperService } from '../../services/modal-helper.service';

@Component({
  selector: 'app-approver-add-comment',
  templateUrl: './approver-add-issue.component.html',
  styleUrls: ['./approver-add-issue.component.scss']
})
export class ApproverAddIssueComponent implements OnInit {

  @Input() system: System;
  activeUser: User;
  classifiers = classifiers;
  isApprovalRequest: boolean;
  openIssuesMatrix: any = null;
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
      case classifiers.issue_type.ESTABLISHMENT_REQUEST.code: {
        this.approvalRequest.title = 'Infosüsteemil puudub asutamise kooskõlastus';
        break;
      }
      case classifiers.issue_type.FINALIZATION_REQUEST.code: {
        this.approvalRequest.title = 'Infosüsteemil puudub lõpetamise kooskõlastus';
        break;
      }
      case classifiers.issue_type.MODIFICATION_REQUEST.code: {
        this.approvalRequest.title = 'Infosüsteemil puudub andmekoosseisu muutmise kooskõlastus';
        break;
      }
      case classifiers.issue_type.TAKE_INTO_USE_REQUEST.code: {
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
    if (this.activeUser.canEdit(this.system.getOwnerCode())){

      this.systemsService.getSystemIssues(this.system.details.short_name).then(res =>{
        this.openIssuesMatrix = {
          establishment: false,
          takingIntoUse: false,
          modification: false,
          finalization: false
        };
        let issues = res.json().content;
        issues.forEach(v => {
          if (v.status == 'OPEN' && v.type != null){
            switch (v.type){
              case classifiers.issue_type.FINALIZATION_REQUEST.code: {
                this.openIssuesMatrix.finalization = true;
                break;
              }
              case classifiers.issue_type.TAKE_INTO_USE_REQUEST.code: {
                this.openIssuesMatrix.takingIntoUse = true;
                break;
              }
              case classifiers.issue_type.MODIFICATION_REQUEST.code: {
                this.openIssuesMatrix.modification = true;
                break;
              }
              case classifiers.issue_type.ESTABLISHMENT_REQUEST.code: {
                this.openIssuesMatrix.establishment = true;
                break;
              }
            }
          }
        });
      });
    }
  }

}
