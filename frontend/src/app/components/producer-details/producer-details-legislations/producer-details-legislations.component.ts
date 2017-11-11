import { Component, OnInit, Input } from '@angular/core';
import { ModalHelperService } from '../../../services/modal-helper.service';
import { ProducerEditLegislationsComponent } from '../../producer-edit/producer-edit-legislations/producer-edit-legislations.component';
import { System } from '../../../models/system';

@Component({
  selector: 'app-producer-details-legislations',
  templateUrl: './producer-details-legislations.component.html',
  styleUrls: ['./producer-details-legislations.component.scss']
})
export class ProducerDetailsLegislationsComponent implements OnInit {

  @Input() system: System;
  @Input() allowEdit: boolean;

  openLegislationsEdit(content) {
    const modalRef = this.modalService.open(ProducerEditLegislationsComponent, {
        backdrop: 'static',
        keyboard: false
      });
    this.system.details.legislations = this.system.details.legislations || [];
    modalRef.componentInstance.system = this.system;
    modalRef.componentInstance.legislations = [].concat(this.system.details.legislations);
    modalRef.result.then( result => {
      if (result.system) {
        this.system = result.system;
      }
    })
  }

  constructor(private modalService: ModalHelperService) { }

  ngOnInit() {
  }

}
