import {Component, Input, OnInit} from '@angular/core';
import {SystemsService} from '../../../services/systems.service';
import {G} from '../../../globals/globals';
import {System} from '../../../models/system';
import {ToastrService} from 'ngx-toastr';
import {GeneralHelperService} from '../../../services/general-helper.service';
import {ModalHelperService} from '../../../services/modal-helper.service';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';

@Component({
  selector: 'app-producer-edit-standard-realisations',
  templateUrl: './producer-edit-standard-realisations.component.html',
  styleUrls: ['./producer-edit-standard-realisations.component.scss']
})
export class ProducerEditStandardRealisationsComponent implements OnInit {

  @Input() system: System;

  globals: any = G;

  realisation: {
    shortName: string,
    differences: string
  };


  createStandardRealisationSystem(addForm){
    if (addForm.valid){
      this.systemsService.createStandardRealisationSystem(
        this.system.details.short_name, this.realisation).then(res => {
      }, err => {
        this.toastrService.error('Serveri viga');
      });
    }
  };

  closeModal(){
    this.modalService.closeActiveModal();
  };

  constructor(private systemsService: SystemsService,
              private modalService: ModalHelperService,
              private generalHelper: GeneralHelperService,
              private toastrService: ToastrService) { }

  ngOnInit() {
    this.realisation = {
      shortName: null,
      differences: null
    };
  }

}
