import { Component, OnInit, Input } from '@angular/core';
import { SystemsService } from '../../../services/systems.service';
import { System } from '../../../models/system';
import { ToastrService } from 'ngx-toastr';
import { GeneralHelperService } from '../../../services/general-helper.service';
import { ModalHelperService } from '../../../services/modal-helper.service';

@Component({
  selector: 'app-producer-edit-legislations',
  templateUrl: './producer-edit-legislations.component.html',
  styleUrls: ['./producer-edit-legislations.component.scss']
})
export class ProducerEditLegislationsComponent implements OnInit {

  @Input() system: System;
  @Input() legislations: any[];
  isChanged: boolean = false;

  data: any = {url: '', name: ''};

  addLegislation(addForm): void {
    if (addForm.valid){
      this.legislations.push({url: this.data.url,
                              name: this.data.name ? this.data.name.trim() : ''});
      this.data = {url: '', name: ''};
      addForm.reset();
      this.isChanged = true;
    }
  }

  deleteLegislation(i): void {
    this.legislations.splice(i, 1);
    this.isChanged = true;
  }

  closeModal(f){
    if (this.isChanged || f.form.dirty){
      if (confirm('Oled väljades muudatusi teinud. Kui navigeerid siit ära ilma salvestamata, siis sinu muudatused kaovad.')){
        this.modalService.dismissActiveModal();
      } else {
        return false;
      }
    } else {
      this.modalService.dismissActiveModal();
    }
  }

  saveSystem(){
    let s = this.generalHelperService.cloneObject(this.system);
    s.details.legislations = this.legislations;
    this.systemsService.updateSystem(s).then(response => {
      this.modalService.closeActiveModal({system: new System(response.json())});
    }, err => {
      this.toastrService.error('Serveri viga.')
    });
  }

  constructor(private modalService: ModalHelperService,
              private systemsService: SystemsService,
              private toastrService: ToastrService,
              private generalHelperService: GeneralHelperService) { }

  ngOnInit() {
  }

}
