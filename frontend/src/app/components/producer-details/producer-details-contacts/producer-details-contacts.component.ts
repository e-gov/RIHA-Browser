import { Component, OnInit, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ProducerEditContactsComponent } from '../../producer-edit/producer-edit-contacts/producer-edit-contacts.component';
import { System } from '../../../models/system';
@Component({
  selector: 'app-producer-details-contacts',
  templateUrl: './producer-details-contacts.component.html',
  styleUrls: ['./producer-details-contacts.component.scss']
})
export class ProducerDetailsContactsComponent implements OnInit {

  @Input() system: System;
  @Input() allowEdit: boolean;

  openContactsEdit(content) {
    const modalRef = this.modalService.open(ProducerEditContactsComponent,{
      backdrop: 'static',
      keyboard: false
    });
    this.system.details.contacts = this.system.details.contacts || [];
    modalRef.componentInstance.system = this.system;
    modalRef.componentInstance.contacts = [].concat(this.system.details.contacts);
  }

  constructor(private modalService: NgbModal) { }

  ngOnInit() {
  }

}
