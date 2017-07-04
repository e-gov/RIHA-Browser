import { Component, OnInit, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ProducerEditTechDocsComponent } from '../../producer-edit/producer-edit-tech-docs/producer-edit-tech-docs.component';
import { System } from '../../../models/system';

@Component({
  selector: 'app-producer-details-tech-docs',
  templateUrl: './producer-details-tech-docs.component.html',
  styleUrls: ['./producer-details-tech-docs.component.scss']
})
export class ProducerDetailsTechDocsComponent implements OnInit {

  @Input() system: System;

  openTechDocsEdit(content) {
    const modalRef = this.modalService.open(ProducerEditTechDocsComponent);
    this.system.details.tech_docs = this.system.details.tech_docs || [];
    modalRef.componentInstance.system = this.system;
    modalRef.componentInstance.tech_docs = [].concat(this.system.details.tech_docs);
  }

  constructor(private modalService: NgbModal) { }

  ngOnInit() {
  }

}
