import { Component, OnInit, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ProducerEditDocumentsComponent } from '../../producer-edit/producer-edit-documents/producer-edit-documents.component';
import { System } from '../../../models/system';

@Component({
  selector: 'app-producer-details-tech-docs',
  templateUrl: './producer-details-documents.component.html',
  styleUrls: ['./producer-details-documents.component.scss']
})
export class ProducerDetailsDocumentsComponent implements OnInit {

  @Input() system: System;
  @Input() allowEdit: boolean;

  openTechDocsEdit(content) {
    const modalRef = this.modalService.open(ProducerEditDocumentsComponent);
    this.system.details.documents = this.system.details.documents || [];
    modalRef.componentInstance.system = this.system;
    modalRef.componentInstance.documents = [].concat(this.system.details.documents);
  }

  constructor(private modalService: NgbModal) { }

  ngOnInit() {
  }

}
