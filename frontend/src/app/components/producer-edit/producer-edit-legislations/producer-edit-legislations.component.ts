import { Component, OnInit, Input } from '@angular/core';
import { SystemsService } from '../../../services/systems.service';
import { System } from '../../../models/system';
import { ToastrService } from 'ngx-toastr';
import { ModalHelperService } from '../../../services/modal-helper.service';
import { GeneralHelperService } from '../../../services/general-helper.service';
import { classifiers } from "../../../services/environment.service";

@Component({
  selector: 'app-producer-edit-legislations',
  templateUrl: './producer-edit-legislations.component.html',
  styleUrls: ['./producer-edit-legislations.component.scss']
})
export class ProducerEditLegislationsComponent implements OnInit {

  @Input() system: System;
  legislations: any[] = [];
  isChanged: boolean = false;
  classifiers = classifiers;

  data: any = {url: '', name: '', type: ''};

  addLegislation(addForm): void {
    if (addForm.valid){
      this.legislations.push({url: this.data.url,
                              name: this.data.name ? this.data.name.trim() : '',
                              type: this.data.type});
      this.data = {url: '', name: '', type: ''};
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
    this.systemsService.getSystem(this.system.details.short_name).subscribe(responseSystem => {
      const system = new System(responseSystem);
      system.details.legislations = this.legislations;
      this.systemsService.updateSystem(system).subscribe(updatedSystem => {
        this.modalService.closeActiveModal({system: new System(updatedSystem)});
      }, err => {
        this.toastrService.error('Serveri viga.');
      });
    }, err => {
      this.toastrService.error('Serveri viga.');
    });
  }

  constructor(private modalService: ModalHelperService,
              private systemsService: SystemsService,
              private toastrService: ToastrService,
              public generalHelperService: GeneralHelperService) { }

  ngOnInit() {
    const system = this.generalHelperService.cloneObject(this.system);
    this.legislations = system.details.legislations || [];
  }

}
