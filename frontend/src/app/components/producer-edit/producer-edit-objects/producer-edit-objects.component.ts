import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { SystemsService } from '../../../services/systems.service';
import { System } from '../../../models/system';
import { Router } from '@angular/router';

@Component({
  selector: 'app-producer-edit-objects',
  templateUrl: './producer-edit-objects.component.html',
  styleUrls: ['./producer-edit-objects.component.scss']
})
export class ProducerEditObjectsComponent implements OnInit {

  @Input() system: System;
  @Input() stored_data: string[];
  @Input() data_files: any[];

  addStoredDataObject(input): void {
    if (input.value.length > 0 && this.stored_data.length < 10){
      this.stored_data.push(input.value);
      input.value = '';
    }
  }

  deleteStoredDataObject(i): void {
    this.stored_data.splice(i, 1);
  }

  addDataFile(url, name): void{
    if (url.value != '' && name.value != ''){
      this.data_files.push({
        url: url.value,
        name: name.value
      });
      url.value = '';
      name.value = '';
    }
  }

  deleteDataFile(i): void{
    this.data_files.splice(i, 1);
  }

  saveSystem(){
    this.system.details.stored_data = this.stored_data;
    this.system.details.data_files = this.data_files;
    this.systemsService.updateSystem(this.system).then(response => {
      this.router.navigate(['/Kirjelda/Vaata/', response.json().details.short_name]);
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
