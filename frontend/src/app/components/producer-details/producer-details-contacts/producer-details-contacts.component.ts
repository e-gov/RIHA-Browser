import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ModalHelperService } from '../../../services/modal-helper.service';
import { ProducerEditContactsComponent } from '../../producer-edit/producer-edit-contacts/producer-edit-contacts.component';
import { System } from '../../../models/system';
import { SystemsService } from '../../../services/systems.service';

@Component({
  selector: 'app-producer-details-contacts',
  templateUrl: './producer-details-contacts.component.html',
  styleUrls: ['./producer-details-contacts.component.scss']
})
export class ProducerDetailsContactsComponent implements OnInit {

  @Input() system: System;
  @Input() allowEdit: boolean;
  @Output() onSystemChanged = new EventEmitter<System>();

  openContactsEdit(content) {
    const modalRef = this.modalService.open(ProducerEditContactsComponent,{
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
              private systemsService: SystemsService) { }

  ngOnInit() {
  }

}
