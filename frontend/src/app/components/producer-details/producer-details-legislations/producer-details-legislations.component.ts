import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ModalHelperService } from '../../../services/modal-helper.service';
import { ProducerEditLegislationsComponent } from '../../producer-edit/producer-edit-legislations/producer-edit-legislations.component';
import { System } from '../../../models/system';
import { SystemsService } from '../../../services/systems.service';
import { ToastrService } from 'ngx-toastr';

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
    this.systemsService.getSystem(this.system.details.short_name).then( res => {
      let system = new System(res.json());
      this.onSystemChanged.emit(system);
      const modalRef = this.modalService.open(ProducerEditLegislationsComponent, {
          backdrop: 'static',
        windowClass: 'fixed-header-modal',
          keyboard: false
        });
      modalRef.componentInstance.system = system;
      modalRef.result.then( result => {
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
