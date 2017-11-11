import { Component, OnInit, Input } from '@angular/core';
import { ModalHelperService } from '../../../services/modal-helper.service';
import { ProducerEditDocumentsComponent } from '../../producer-edit/producer-edit-documents/producer-edit-documents.component';
import { System } from '../../../models/system';
import { GeneralHelperService } from '../../../services/general-helper.service';

@Component({
  selector: 'app-producer-details-tech-docs',
  templateUrl: './producer-details-documents.component.html',
  styleUrls: ['./producer-details-documents.component.scss']
})
export class ProducerDetailsDocumentsComponent implements OnInit {

  @Input() system: System;
  @Input() allowEdit: boolean;

  openTechDocsEdit(content) {
    const modalRef = this.modalService.open(ProducerEditDocumentsComponent,{
      backdrop: 'static',
      keyboard: false
    });
    this.system.details.documents = this.system.details.documents || [];
    modalRef.componentInstance.system = this.system;
    modalRef.componentInstance.documents = [].concat(this.system.details.documents);
    modalRef.result.then( result => {
      if (result.system) {
        this.system = result.system;
      }
    })
  }

  constructor(private modalService: ModalHelperService,
              public generalHelperService: GeneralHelperService) { }

  ngOnInit() {
  }

}
