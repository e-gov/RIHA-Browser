import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { SystemsService } from '../../services/systems.service';
import { System } from '../../models/system';
import { Router } from '@angular/router';

@Component({
  selector: 'app-producer-edit-objects',
  templateUrl: './producer-edit-objects.component.html',
  styleUrls: ['./producer-edit-objects.component.scss']
})
export class ProducerEditObjectsComponent implements OnInit {

  @Input() system: System;
  @Input() objects: string[];

  addObject(input): void {
    if (input.value.length > 0){
      this.objects.push(input.value);
      input.value = '';
    }
  }

  deleteObject(i): void {
    this.objects.splice(i, 1);
  }

  saveSystem(){
    this.system.details.objects = this.objects;
    this.systemsService.updateSystem(this.system).then(response => {
      this.router.navigate(['/Kirjelda/Vaata/', response.json().id]);
    });
    this.activeModal.close('saved');
  }

  constructor(public activeModal: NgbActiveModal,
              private systemsService: SystemsService,
              private router: Router) {
  }

  ngOnInit() {
  }

}
