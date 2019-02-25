import { Component, EventEmitter, OnInit, Input, Output } from '@angular/core';
import { ModalHelperService } from '../../../services/modal-helper.service';
import { System } from '../../../models/system';
import { GeneralHelperService } from '../../../services/general-helper.service';
import { SystemsService } from '../../../services/systems.service';
import { ToastrService } from 'ngx-toastr';
import { ProducerEditSecurityComponent } from '../../producer-edit/producer-edit-security/producer-edit-security.component';
import { EnvironmentService, globals } from '../../../services/environment.service';

@Component({
  selector: 'app-producer-details-security',
  templateUrl: './producer-details-security.component.html',
  styleUrls: ['./producer-details-security.component.scss']
})
export class ProducerDetailsSecurityComponent implements OnInit {

  @Input() system: System;
  @Input() allowEdit: boolean;
  @Output() onSystemChanged = new EventEmitter<System>();

  globals = globals;

  isAuditVisible(){
    return this.environmentService.getUserMatrix().hasApproverRole || this.allowEdit;
  }

  openSecurityEdit(content) {
    this.systemsService.getSystem(this.system.details.short_name).then( res => {
      let system = new System(res.json());
      this.onSystemChanged.emit(system);
      const modalRef = this.modalService.open(ProducerEditSecurityComponent ,{
        backdrop: 'static',
        windowClass: 'fixed-header-modal',
        keyboard: false
      });
      modalRef.componentInstance.system = this.system;
      modalRef.result.then( result => {
        if (result.system) {
          this.onSystemChanged.emit(result.system);
        }
      }, reason => {

      });
    }, err => {
      this.toastrService.error('Serveri viga.');
    });
  }

  constructor(private modalService: ModalHelperService,
              private environmentService: EnvironmentService,
              public generalHelperService: GeneralHelperService,
              private systemsService: SystemsService,
              private toastrService: ToastrService) {

  }

  ngOnInit() {
  }

}
