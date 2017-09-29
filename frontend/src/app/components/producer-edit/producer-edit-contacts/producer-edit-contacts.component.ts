import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { SystemsService } from '../../../services/systems.service';
import { System } from '../../../models/system';
import { Router } from '@angular/router';

@Component({
  selector: 'app-producer-edit-contacts',
  templateUrl: './producer-edit-contacts.component.html',
  styleUrls: ['./producer-edit-contacts.component.scss']
})
export class ProducerEditContactsComponent implements OnInit {

  @Input() system: System;
  @Input() contacts: any[];

  data: any = {email: '', name: ''};

  addContact(addForm): void {
    if (addForm.valid){
      this.contacts.push({email: this.data.email,
        name: this.data.name ? this.data.name.trim() : ''});
      this.data = {email: '', name: ''};
      addForm.reset();
    }
  }

  deleteContact(i): void {
    this.contacts.splice(i, 1);
  }

  saveSystem(){
    this.system.details.contacts = this.contacts;
    this.systemsService.updateSystem(this.system).then(response => {
      this.router.navigate(['/Kirjelda/Vaata/', response.json().details.short_name]);
    });
    this.activeModal.close('saved');
  }

  constructor(public activeModal: NgbActiveModal,
              private systemsService: SystemsService,
              private router: Router) { }

  ngOnInit() {
  }

}
