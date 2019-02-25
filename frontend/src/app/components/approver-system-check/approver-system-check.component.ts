import { Component, OnInit, Input } from '@angular/core';
import { System } from '../../models/system';
import { SystemsService } from '../../services/systems.service';
import { globals } from "../../services/environment.service";
import { GeneralHelperService } from '../../services/general-helper.service';

@Component({
  selector: 'app-approver-system-check',
  templateUrl: './approver-system-check.component.html',
  styleUrls: ['./approver-system-check.component.scss']
})
export class ApproverSystemCheckComponent implements OnInit {

  @Input() system: System;

  public systemCheckMatrix: any;
  public hasErrors: boolean = false;

  private getCheckTimeout(){
    return Math.floor(Math.random() * 500) + 200;
  }

  private startSystemCheck(){
    this.hasErrors = false;

    let statusPending = globals.system_check_status.PENDING;
    let statusInProgress = globals.system_check_status.IN_PROGRESS;
    this.systemCheckMatrix = {
      systemStatus: statusInProgress,
      developmentStatus: statusPending,
      storedDataStatus: statusPending,
      dataFilesStatus: statusPending,
      legislationsStatus: statusPending,
      documentsStatus: statusPending,
      contactsStatus: statusPending
    };

    this.systemsService.getSystem(this.system.details.uuid).then(res => {
      this.system = new System(res.json());
      this.checkSystemStatus();
    }, err => {
      this.helper.showError();
      let statusCancelled = globals.system_check_status.CANCELLED;
      this.systemCheckMatrix = {
        systemStatus: statusCancelled,
        developmentStatus: statusCancelled,
        storedDataStatus: statusCancelled,
        dataFilesStatus: statusCancelled,
        legislationsStatus: statusCancelled,
        documentsStatus: statusCancelled,
        contactsStatus: statusCancelled
      };
    });
  }

  private checkSystemStatus(){
    if (this.system.details.meta && this.system.details.meta.system_status && this.system.details.meta.system_status.status){
      this.systemCheckMatrix.systemStatus = globals.system_check_status.PASSED;
    } else {
      this.systemCheckMatrix.systemStatus = globals.system_check_status.FAILED;
    }
    this.systemCheckMatrix.developmentStatus = globals.system_check_status.IN_PROGRESS;
    setTimeout(()=> {
      this.checkDevelopmentStatus();
    }, this.getCheckTimeout());
  }

  private checkDevelopmentStatus(){
    if (this.system.details.meta && this.system.details.meta.development_status){
      this.systemCheckMatrix.developmentStatus = globals.system_check_status.PASSED;
    } else {
      this.systemCheckMatrix.developmentStatus = globals.system_check_status.FAILED;
    }
    this.systemCheckMatrix.storedDataStatus = globals.system_check_status.IN_PROGRESS;
    setTimeout(()=> {
      this.checkStoredData();
    }, this.getCheckTimeout());
  }

  private checkStoredData(){
    if (this.system.details.stored_data && this.system.details.stored_data.length > 0){
      this.systemCheckMatrix.storedDataStatus = globals.system_check_status.PASSED;
    } else {
      this.systemCheckMatrix.storedDataStatus = globals.system_check_status.FAILED;
    }
    this.systemCheckMatrix.dataFilesStatus = globals.system_check_status.IN_PROGRESS;
    setTimeout(()=> {
      this.checkDataFiles();
    }, this.getCheckTimeout());
  }

  private checkDataFiles(){
    if (this.system.details.data_files && this.system.details.data_files.length > 0){
      this.systemCheckMatrix.dataFilesStatus = globals.system_check_status.PASSED;
    } else {
      this.systemCheckMatrix.dataFilesStatus = globals.system_check_status.FAILED;
    }
    this.systemCheckMatrix.legislationsStatus = globals.system_check_status.IN_PROGRESS;
    setTimeout(()=> {
      this.checkLegislations();
    }, this.getCheckTimeout());
  }

  private checkLegislations(){
    if (this.system.details.legislations && this.system.details.legislations.length > 0){
      this.systemCheckMatrix.legislationsStatus = globals.system_check_status.PASSED;
    } else {
      this.systemCheckMatrix.legislationsStatus = globals.system_check_status.FAILED;
    }
    this.systemCheckMatrix.documentsStatus = globals.system_check_status.IN_PROGRESS;
    setTimeout(()=> {
      this.checkDocuments();
    }, this.getCheckTimeout());
  }

  private checkDocuments(){
    if (this.system.details.documents && this.system.details.documents.length > 0){
      this.systemCheckMatrix.documentsStatus = globals.system_check_status.PASSED;
    } else {
      this.systemCheckMatrix.documentsStatus = globals.system_check_status.FAILED;
    }
    this.systemCheckMatrix.contactsStatus = globals.system_check_status.IN_PROGRESS;
    setTimeout(()=> {
      this.checkContacts();
    }, this.getCheckTimeout());
  }

  private checkContacts(){
    if (this.system.details.contacts && this.system.details.contacts.length > 0){
      this.systemCheckMatrix.contactsStatus = globals.system_check_status.PASSED;
    } else {
      this.systemCheckMatrix.contactsStatus = globals.system_check_status.FAILED;
    }
    this.setErrorStatus();
  }

  private setErrorStatus(){
    for (let k in this.systemCheckMatrix) {
      if (this.systemCheckMatrix[k] == globals.system_check_status.FAILED){
        this.hasErrors = true;
      }
    }
  }

  constructor(private systemsService: SystemsService,
              private helper: GeneralHelperService) { }

  ngOnInit() {
    this.startSystemCheck();
  }

}
