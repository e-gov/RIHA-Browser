import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { SystemsService } from '../../../services/systems.service';
import { System } from '../../../models/system';
import { ToastrService } from 'ngx-toastr';
import { GeneralHelperService } from '../../../services/general-helper.service';

@Component({
  selector: 'app-producer-edit-contacts',
  templateUrl: './producer-edit-contacts.component.html',
  styleUrls: ['./producer-edit-contacts.component.scss']
})
export class ProducerEditContactsComponent implements OnInit {

  @Input() system: System;
  @Input() contacts: any[];
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
    let s = this.generalHelperService.cloneObject(this.system);
    s.details.contacts = this.contacts;
    this.systemsService.updateSystem(s).then(response => {
      this.activeModal.close({system: new System(response.json())});
    }, err => {
      this.toastrService.error('Serveri viga.');
    });
  }

  closeModal(f){
    if (this.isChanged || f.form.dirty){
      if (confirm('Oled väljades muudatusi teinud. Kui navigeerid siit ära ilma salvestamata, siis sinu muudatused kaovad.')){
        this.activeModal.dismiss();
      } else {
        return false;
      }
    } else {
      this.activeModal.dismiss();
    }
  }

  constructor(private activeModal: NgbActiveModal,
              private systemsService: SystemsService,
              private toastrService: ToastrService,
              private generalHelperService: GeneralHelperService) { }

  ngOnInit() {
  }

}
