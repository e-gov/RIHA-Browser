import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ModalHelperService } from '../../../services/modal-helper.service';
import { ProducerEditDocumentsComponent } from '../../producer-edit/producer-edit-documents/producer-edit-documents.component';
import { System } from '../../../models/system';
import { GeneralHelperService } from '../../../services/general-helper.service';
import { SystemsService } from '../../../services/systems.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-producer-details-tech-docs',
  templateUrl: './producer-details-documents.component.html',
  styleUrls: ['./producer-details-documents.component.scss']
})
export class ProducerDetailsDocumentsComponent implements OnInit {

  @Input() system: System;
  @Input() allowEdit: boolean;
  @Output() onSystemChanged = new EventEmitter<System>();

  openTechDocsEdit(content) {
    this.systemsService.getSystem(this.system.details.short_name).then( res => {
      let system = new System(res.json());
      this.onSystemChanged.emit(system);
      const modalRef = this.modalService.open(ProducerEditDocumentsComponent,{
        backdrop: 'static',
        windowClass: 'fixed-header-modal',
        keyboard: false
      });
      modalRef.componentInstance.system = system;
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
              public generalHelperService: GeneralHelperService,
              private systemsService: SystemsService,
              private toastrService: ToastrService) { }

  ngOnInit() {
  }

}
