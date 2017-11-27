import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ModalHelperService } from '../../../services/modal-helper.service';
import { ProducerEditDocumentsComponent } from '../../producer-edit/producer-edit-documents/producer-edit-documents.component';
import { System } from '../../../models/system';
import { GeneralHelperService } from '../../../services/general-helper.service';
import { SystemsService } from '../../../services/systems.service';

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
    const modalRef = this.modalService.open(ProducerEditDocumentsComponent,{
      backdrop: 'static',
      keyboard: false
    });
    modalRef.componentInstance.system = this.system;
    modalRef.result.then( result => {
      if (result.system) {
        this.onSystemChanged.emit(result.system);
      }
    }, reason => {
      this.systemsService.getSystem(this.system.details.short_name).then(
        res => {
          this.onSystemChanged.emit(new System(res.json()));
        }, err => {

        }
      )
    });
  }

  constructor(private modalService: ModalHelperService,
              public generalHelperService: GeneralHelperService,
              private systemsService: SystemsService) { }

  ngOnInit() {
  }

}
