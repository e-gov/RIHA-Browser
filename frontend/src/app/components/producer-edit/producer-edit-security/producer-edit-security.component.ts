import { Component, OnInit, Input } from '@angular/core';
import { ModalHelperService } from '../../../services/modal-helper.service';
import { GeneralHelperService } from '../../../services/general-helper.service';
import { System } from '../../../models/system';
import { SystemsService } from '../../../services/systems.service';
import { ToastrService } from 'ngx-toastr';
import { classifiers } from "../../../services/environment.service";

@Component({
  selector: 'app-producer-edit-security',
  templateUrl: './producer-edit-security.component.html',
  styleUrls: ['./producer-edit-security.component.scss']
})
export class ProducerEditSecurityComponent implements OnInit {

  @Input() system: System;
  security: any;
  classifiers = classifiers;

  hasSecurity: boolean = false;
  isAuditApplied: boolean = false;
  isIske: boolean;
  securityClass: {
    k: any,
    t: any,
    s: any
  };

  getSecurityLevel(){
    if (this.securityClass.k == 3 || this.securityClass.t == 3 || this.securityClass.s == 3){
      return 'H';
    } else if (this.securityClass.k == 2 || this.securityClass.t == 2 || this.securityClass.s == 2){
      return 'M';
    } else {
      return 'L';
    }
  }

  getSecurityClass(){
      return `K${this.securityClass.k}T${this.securityClass.t}S${this.securityClass.s}`;
  }

  saveSystem(f){
    if (f.valid){
      this.systemsService.getSystem(this.system.details.short_name).then(res =>{
        let s = new System(res.json());
        s.details.security = this.prepareSecurityInfoForSending(this.security);
        this.systemsService.updateSystem(s).then(response => {
          this.modalService.closeActiveModal({system: new System(response.json())});
        }, err => {
          this.toastrService.error('Serveri viga.');
        });
      }, err => {
        this.toastrService.error('Serveri viga.');
      });
    }
  }

  closeModal(f){
    if (f.form.dirty){
      if (confirm('Oled väljades muudatusi teinud. Kui navigeerid siit ära ilma salvestamata, siis sinu muudatused kaovad.')){
        this.modalService.dismissActiveModal();
      } else {
        return false;
      }
    } else {
      this.modalService.dismissActiveModal();
    }
  }

  resetStandard(){
    this.security.standard = null;
  }

  resetAuditInfo(){
    this.security.latest_audit_date = null;
    this.security.latest_audit_resolution = null;
  }

  private prepareSecurityInfoForSending(security){
    security = this.generalHelperService.cloneObject(security);
    if (this.hasSecurity == false){
      security = {
        class: null,
        level: null,
        standard: null,
        latest_audit_resolution: null,
        latest_audit_date: null
      }
    } else {
      if (security.latest_audit_date){
        security.latest_audit_date = this.generalHelperService.dateObjToTimestamp(security.latest_audit_date);
      }
      if (this.isIske){
        security.standard = this.classifiers.security_standard.iske.code;
      }
      if (this.isIske && this.securityClass.k != null && this.securityClass.s != null && this.securityClass.t != null){
        security.class = this.getSecurityClass();
        security.level = this.getSecurityLevel();
      } else {
        security.class = null;
        security.level = null;
      }
      if (this.isAuditApplied == false){
        security.latest_audit_resolution = null;
        security.latest_audit_date = null;
      }
    }
    return security;
  }

  constructor(private modalService: ModalHelperService,
              private systemsService: SystemsService,
              private toastrService: ToastrService,
              private generalHelperService: GeneralHelperService) { }

  ngOnInit(){
    let system = this.generalHelperService.cloneObject(this.system);
    this.security = system.details.security || {
      standard: null,
      class: null,
      level: null,
      latest_audit_date: null,
      latest_audit_resolution: null
    };

    this.hasSecurity = this.security.standard != null;
    this.isIske = this.security.standard == 'ISKE';
    this.isAuditApplied = this.security.latest_audit_date || this.security.latest_audit_resolution;

    if (this.security.class && this.security.class.length == 6){
      this.securityClass = {
        k: this.security.class.substr(1, 1),
        t: this.security.class.substr(3, 1),
        s: this.security.class.substr(5, 1)
      }
    } else {
      this.securityClass = {
        k: null,
        t: null,
        s: null
      }
    }

    this.security.latest_audit_date = this.generalHelperService.timestampToDateObj(this.security.latest_audit_date);
  };

}
