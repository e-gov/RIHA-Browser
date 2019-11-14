import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {ModalHelperService} from '../../../services/modal-helper.service';
import {GeneralHelperService} from '../../../services/general-helper.service';
import {System} from '../../../models/system';
import {SystemsService} from '../../../services/systems.service';
import {ToastrService} from 'ngx-toastr';
import {classifiers} from "../../../services/environment.service";
import {Observable} from "rxjs";
import {NgForm} from "@angular/forms";
import {CanDeactivateModal} from '../../../guards/can-deactivate-modal.guard';

@Component({
  selector: 'app-producer-edit-security',
  templateUrl: './producer-edit-security.component.html',
  styleUrls: ['./producer-edit-security.component.scss']
})
export class ProducerEditSecurityComponent implements OnInit, CanDeactivateModal {

  @ViewChild('securityForm', null) formObject: NgForm;

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
      this.systemsService.getSystem(this.system.details.short_name).subscribe(responseSystem => {
        const system = new System(responseSystem);
        system.details.security = this.prepareSecurityInfoForSending(this.security);
        this.systemsService.updateSystem(system).subscribe(updatedSystem => {
          this.modalService.closeActiveModal({system: new System(updatedSystem)});
        }, err => {
          this.toastrService.error('Serveri viga.');
        });
      }, err => {
        this.toastrService.error('Serveri viga.');
      });
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
        security.standard = this.classifiers.security_standard.ISKE.code;
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

  canDeactivate(): Observable<boolean> | Promise<boolean> | boolean {
    return this.closeModal();
  }

  closeModal() {
    if (this.isFormChanged) {
      const observer = this.modalService.confirm('Oled väljades muudatusi teinud. Kui navigeerid siit ära ilma salvestamata, siis sinu muudatused kaovad.');
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
    return this.formObject.form.dirty
  }

  constructor(private modalService: ModalHelperService,
              private systemsService: SystemsService,
              private toastrService: ToastrService,
              private generalHelperService: GeneralHelperService) { }

  ngOnInit(){
    const system = this.generalHelperService.cloneObject(this.system);
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
