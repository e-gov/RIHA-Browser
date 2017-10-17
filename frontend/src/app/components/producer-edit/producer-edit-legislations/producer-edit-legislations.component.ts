import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { SystemsService } from '../../../services/systems.service';
import { System } from '../../../models/system';
import { Router } from '@angular/router';

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
        this.activeModal.close();
      } else {
        return false;
      }
    } else {
      this.activeModal.close();
    }
  }

  saveSystem(){
    this.system.details.legislations = this.legislations;
    this.systemsService.updateSystem(this.system).then(response => {
      this.router.navigate(['/Kirjelda/Vaata/', response.json().details.short_name]);
    });
    this.activeModal.close('saved');
  }

  constructor(private activeModal: NgbActiveModal,
              private systemsService: SystemsService,
              private router: Router) { }

  ngOnInit() {
  }

}
