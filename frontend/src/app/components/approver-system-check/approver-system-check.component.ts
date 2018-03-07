import { Component, OnInit, Input } from '@angular/core';
import { System } from '../../models/system';
import { SystemsService } from '../../services/systems.service';
import { G } from '../../globals/globals';
import { GeneralHelperService } from '../../services/general-helper.service';

@Component({
  selector: 'app-approver-system-check',
  templateUrl: './approver-system-check.component.html',
  styleUrls: ['./approver-system-check.component.scss']
})
export class ApproverSystemCheckComponent implements OnInit {

  @Input() system: System;

  private globals: any = G;
  public systemCheckMatrix: any = {
    systemStatus: G.system_check_status.PENDING,
    developmentStatus: G.system_check_status.PENDING,
    storedDataStatus: G.system_check_status.PENDING,
    dataFilesStatus: G.system_check_status.PENDING,
    legislationsStatus: G.system_check_status.PENDING,
    documentsStatus: G.system_check_status.PENDING,
    contactsStatus: G.system_check_status.PENDING
  };
  public hasErrors: boolean = false;

  private getCheckTimeout(){
    return Math.floor(Math.random() * 500) + 200;
  }

  private startSystemCheck(){
    this.hasErrors = false;

    this.systemCheckMatrix = {
      systemStatus: G.system_check_status.IN_PROGRESS,
      developmentStatus: G.system_check_status.PENDING,
      storedDataStatus: G.system_check_status.PENDING,
      dataFilesStatus: G.system_check_status.PENDING,
      legislationsStatus: G.system_check_status.PENDING,
      documentsStatus: G.system_check_status.PENDING,
      contactsStatus: G.system_check_status.PENDING
    };

    this.systemsService.getSystem(this.system.details.uuid).then(res => {
      this.system = new System(res.json());
      this.checkSystemStatus();
    }, err => {
      this.helper.showError();
      this.systemCheckMatrix = {
        systemStatus: G.system_check_status.CANCELLED,
        developmentStatus: G.system_check_status.CANCELLED,
        storedDataStatus: G.system_check_status.CANCELLED,
        dataFilesStatus: G.system_check_status.CANCELLED,
        legislationsStatus: G.system_check_status.CANCELLED,
        documentsStatus: G.system_check_status.CANCELLED,
        contactsStatus: G.system_check_status.CANCELLED
      };
    });
  }

  private checkSystemStatus(){
    if (this.system.details.meta && this.system.details.meta.system_status && this.system.details.meta.system_status.status){
      this.systemCheckMatrix.systemStatus = this.globals.system_check_status.PASSED;
    } else {
      this.systemCheckMatrix.systemStatus = this.globals.system_check_status.FAILED;
    }
    this.systemCheckMatrix.developmentStatus = this.globals.system_check_status.IN_PROGRESS;
    setTimeout(()=> {
      this.checkDevelopmentStatus();
    }, this.getCheckTimeout());
  }

  private checkDevelopmentStatus(){
    if (this.system.details.meta && this.system.details.meta.development_status){
      this.systemCheckMatrix.developmentStatus = this.globals.system_check_status.PASSED;
    } else {
      this.systemCheckMatrix.developmentStatus = this.globals.system_check_status.FAILED;
    }
    this.systemCheckMatrix.storedDataStatus = this.globals.system_check_status.IN_PROGRESS;
    setTimeout(()=> {
      this.checkStoredData();
    }, this.getCheckTimeout());
  }

  private checkStoredData(){
    if (this.system.details.stored_data && this.system.details.stored_data.length > 0){
      this.systemCheckMatrix.storedDataStatus = this.globals.system_check_status.PASSED;
    } else {
      this.systemCheckMatrix.storedDataStatus = this.globals.system_check_status.FAILED;
    }
    this.systemCheckMatrix.dataFilesStatus = this.globals.system_check_status.IN_PROGRESS;
    setTimeout(()=> {
      this.checkDataFiles();
    }, this.getCheckTimeout());
  }

  private checkDataFiles(){
    if (this.system.details.data_files && this.system.details.data_files.length > 0){
      this.systemCheckMatrix.dataFilesStatus = this.globals.system_check_status.PASSED;
    } else {
      this.systemCheckMatrix.dataFilesStatus = this.globals.system_check_status.FAILED;
    }
    this.systemCheckMatrix.legislationsStatus = this.globals.system_check_status.IN_PROGRESS;
    setTimeout(()=> {
      this.checkLegislations();
    }, this.getCheckTimeout());
  }

  private checkLegislations(){
    if (this.system.details.legislations && this.system.details.legislations.length > 0){
      this.systemCheckMatrix.legislationsStatus = this.globals.system_check_status.PASSED;
    } else {
      this.systemCheckMatrix.legislationsStatus = this.globals.system_check_status.FAILED;
    }
    this.systemCheckMatrix.documentsStatus = this.globals.system_check_status.IN_PROGRESS;
    setTimeout(()=> {
      this.checkDocuments();
    }, this.getCheckTimeout());
  }

  private checkDocuments(){
    if (this.system.details.documents && this.system.details.documents.length > 0){
      this.systemCheckMatrix.documentsStatus = this.globals.system_check_status.PASSED;
    } else {
      this.systemCheckMatrix.documentsStatus = this.globals.system_check_status.FAILED;
    }
    this.systemCheckMatrix.contactsStatus = this.globals.system_check_status.IN_PROGRESS;
    setTimeout(()=> {
      this.checkContacts();
    }, this.getCheckTimeout());
  }

  private checkContacts(){
    if (this.system.details.contacts && this.system.details.contacts.length > 0){
      this.systemCheckMatrix.contactsStatus = this.globals.system_check_status.PASSED;
    } else {
      this.systemCheckMatrix.contactsStatus = this.globals.system_check_status.FAILED;
    }
    this.setErrorStatus();
  }

  private setErrorStatus(){
    for (let k in this.systemCheckMatrix) {
      if (this.systemCheckMatrix[k] == this.globals.system_check_status.FAILED){
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
