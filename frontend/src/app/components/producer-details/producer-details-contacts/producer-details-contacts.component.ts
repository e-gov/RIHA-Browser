import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ModalHelperService } from '../../../services/modal-helper.service';
import { ProducerEditContactsComponent } from '../../producer-edit/producer-edit-contacts/producer-edit-contacts.component';
import { System } from '../../../models/system';
import { SystemsService } from '../../../services/systems.service';
import { ToastrService } from 'ngx-toastr';

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
    this.systemsService.getSystem(this.system.details.short_name).subscribe( responseSystem => {
      const system = new System(responseSystem);
      this.onSystemChanged.emit(system);
      const modalRef = this.modalService.open(ProducerEditContactsComponent, {
        backdrop: 'static',
        windowClass: 'fixed-header-modal',
        keyboard: false
      });
      modalRef.componentInstance.system = system;
      modalRef.result.then(result => {
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
              private systemsService: SystemsService,
              private toastrService: ToastrService) { }

  ngOnInit() {
  }

}
