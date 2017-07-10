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

  addLegislation(urlInput, nameInput): void {
    if (urlInput.value.length > 0 && urlInput.checkValidity()){
      this.legislations.push({url: urlInput.value,
                              name: nameInput.value.trim()});
      urlInput.value = '';
      nameInput.value = '';
    }
  }

  deleteLegislation(i): void {
    this.legislations.splice(i, 1);
  }

  saveSystem(){
    this.system.details.legislations = this.legislations;
    this.systemsService.updateSystem(this.system).then(response => {
      this.router.navigate(['/Kirjelda/Vaata/', response.json().id]);
    });
    this.activeModal.close('saved');
  }

  constructor(public activeModal: NgbActiveModal,
              private systemsService: SystemsService,
              private router: Router) { }

  ngOnInit() {
  }

}
