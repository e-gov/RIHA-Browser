import { Component, EventEmitter, OnInit, Input, Output } from '@angular/core';
import { ModalHelperService } from '../../../services/modal-helper.service';
import { ProducerEditObjectsComponent } from '../../producer-edit/producer-edit-objects/producer-edit-objects.component';
import { System } from '../../../models/system';
import { GeneralHelperService } from '../../../services/general-helper.service';
import { SystemsService } from '../../../services/systems.service';

@Component({
  selector: 'app-producer-details-objects',
  templateUrl: './producer-details-objects.component.html',
  styleUrls: ['./producer-details-objects.component.scss']
})
export class ProducerDetailsObjectsComponent implements OnInit {

  @Input() system: System;
  @Input() allowEdit: boolean;
  @Output() onSystemChanged = new EventEmitter<System>();

  openObjectsEdit(content) {
    const modalRef = this.modalService.open(ProducerEditObjectsComponent,{
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
              public generalHelperService: GeneralHelperService,
              private systemsService: SystemsService) {

  }

  ngOnInit() {
  }

}
