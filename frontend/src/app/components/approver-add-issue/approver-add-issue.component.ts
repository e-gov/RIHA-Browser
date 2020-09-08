import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {SystemsService} from '../../services/systems.service';
import {classifiers, EnvironmentService} from '../../services/environment.service';
import {System} from '../../models/system';
import {ToastrService} from 'ngx-toastr';
import {User} from '../../models/user';
import {ModalHelperService} from '../../services/modal-helper.service';
import {CanDeactivateModal} from '../../guards/can-deactivate-modal.guard';
import {Observable} from "rxjs";
import {NgForm} from "@angular/forms";
import {CONSTANTS} from '../../utils/constants';

@Component({
  selector: 'app-approver-add-comment',
  templateUrl: './approver-add-issue.component.html',
  styleUrls: ['./approver-add-issue.component.scss']
})
export class ApproverAddIssueComponent implements OnInit, CanDeactivateModal {

  @ViewChild('approvalRequestForm') formObjectApprovalRequest: NgForm;
  @ViewChild('newIssueForm') formObjectNewIssue: NgForm;

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
      this.systemsService.addSystemIssue(this.system.details.short_name, f.value).subscribe(
        issue => {
          this.modalService.closeActiveModal();
        },
        err => {
          this.toastrService.error('Serveri viga! Proovige uuesti!');
        })
    }
  }

  onSubmitApprovalRequest(f) :void {
    if (f.valid) {
      this.systemsService.addSystemIssue(this.system.details.short_name, this.approvalRequest).subscribe(
        issue => {
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

  canSwitchViews(){
    return this.environmentService.getUserMatrix().hasApproverRole && this.activeUser.canEdit(this.system.getOwnerCode());
  }

  canDeactivate(): Observable<boolean> | Promise<boolean> | boolean {
    return this.closeModal();
  }

  closeModal() {
    if (this.isFormChanged) {
      const observer = this.modalService.confirm(CONSTANTS.CLOSE_DIALOG_WARNING);
      observer.subscribe(confirmed => {
        if (confirmed) {
          this.modalService.dismissActiveModal();
        }
      });
      return observer;
    }

    this.modalService.dismissActiveModal();
    return true;
  }

  /**
   * Getters
   */

  /**
   * Is form data changed ?
   */
  get isFormChanged(): boolean {
    if (this.isApprovalRequest) {
      return this.formObjectApprovalRequest.form.dirty;
    } else {
      return this.formObjectNewIssue.form.dirty;
    }
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

      this.systemsService.getSystemIssues(this.system.details.short_name).subscribe(res =>{
        this.openIssuesMatrix = {
          establishment: false,
          takingIntoUse: false,
          modification: false,
          finalization: false
        };

        res.content.forEach(issue => {
          if (issue.status == 'OPEN' && issue.type != null){
            switch (issue.type){
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
