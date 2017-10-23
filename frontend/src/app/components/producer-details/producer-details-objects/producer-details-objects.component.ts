import { Component, OnInit, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ProducerEditObjectsComponent } from '../../producer-edit/producer-edit-objects/producer-edit-objects.component';
import { System } from '../../../models/system';
import { GeneralHelperService } from '../../../services/general-helper.service';

@Component({
  selector: 'app-producer-details-objects',
  templateUrl: './producer-details-objects.component.html',
  styleUrls: ['./producer-details-objects.component.scss']
})
export class ProducerDetailsObjectsComponent implements OnInit {

  @Input() system: System;
  @Input() allowEdit: boolean;

  openObjectsEdit(content) {
    const modalRef = this.modalService.open(ProducerEditObjectsComponent,{
      backdrop: 'static',
      keyboard: false
    });
    modalRef.componentInstance.system = this.system;
    this.system.details.stored_data = this.system.details.stored_data || [];
    modalRef.componentInstance.stored_data = [].concat(this.system.details.stored_data);
    this.system.details.data_files = this.system.details.data_files || [];
    modalRef.componentInstance.data_files = [].concat(this.system.details.data_files);
  }

  constructor(private modalService: NgbModal,
              public generalHelperService: GeneralHelperService) {

  }

  ngOnInit() {
  }

}
