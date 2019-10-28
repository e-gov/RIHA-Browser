import { Component, OnInit, Input } from '@angular/core';
import { SystemsService } from '../../../services/systems.service';
import { System } from '../../../models/system';
import { ToastrService } from 'ngx-toastr';
import { ModalHelperService } from '../../../services/modal-helper.service';
import { GeneralHelperService } from '../../../services/general-helper.service';

@Component({
  selector: 'app-producer-edit-contacts',
  templateUrl: './producer-edit-contacts.component.html',
  styleUrls: ['./producer-edit-contacts.component.scss']
})
export class ProducerEditContactsComponent implements OnInit {

  @Input() system: System;
  contacts: any[] = [];
  isChanged: boolean = false;

  data: any = {email: '', name: ''};

  addContact(addForm): void {
    if (addForm.valid){
      this.contacts.push({email: this.data.email,
        name: this.data.name ? this.data.name.trim() : ''});
      this.data = {email: '', name: ''};
      addForm.reset();
      this.isChanged = true;
    }
  }

  deleteContact(i): void {
    this.contacts.splice(i, 1);
    this.isChanged = true;
  }

  saveSystem(){
    this.systemsService.getSystem(this.system.details.short_name).subscribe(responseSystem => {
      const system = new System(responseSystem);
      system.details.contacts = this.contacts;
      this.systemsService.updateSystem(system).subscribe(updatedSystem => {
        this.modalService.closeActiveModal({system: new System(updatedSystem)});
      }, err => {
        this.toastrService.error('Serveri viga.');
      });
    }, err => {
      this.toastrService.error('Serveri viga.');
    });
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

  constructor(private modalService: ModalHelperService,
              private systemsService: SystemsService,
              private toastrService: ToastrService,
              private generalHelperService: GeneralHelperService) { }

  ngOnInit() {
    const system = this.generalHelperService.cloneObject(this.system);
    this.contacts = system.details.contacts || [];
  }
}
