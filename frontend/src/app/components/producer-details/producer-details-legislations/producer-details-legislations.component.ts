import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ModalHelperService } from '../../../services/modal-helper.service';
import { ProducerEditLegislationsComponent } from '../../producer-edit/producer-edit-legislations/producer-edit-legislations.component';
import { System } from '../../../models/system';
import { SystemsService } from '../../../services/systems.service';

@Component({
  selector: 'app-producer-details-legislations',
  templateUrl: './producer-details-legislations.component.html',
  styleUrls: ['./producer-details-legislations.component.scss']
})
export class ProducerDetailsLegislationsComponent implements OnInit {

  @Input() system: System;
  @Input() allowEdit: boolean;
  @Output() onSystemChanged = new EventEmitter<System>();

  openLegislationsEdit(content) {
    const modalRef = this.modalService.open(ProducerEditLegislationsComponent, {
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
