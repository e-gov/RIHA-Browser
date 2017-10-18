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
  isChanged: boolean = false;

  dataFile: any = null;
  uploading: boolean = false;
  data: any = {
    name: '',
    url: ''
  };

  // string data objects

  addStoredDataObject(input): void {
    if (input.value.length > 0 && this.stored_data.length < 10){
      this.stored_data.push(input.value);
      input.value = '';
      this.isChanged = true;
    }
  }

  deleteStoredDataObject(i): void {
    this.stored_data.splice(i, 1);
    this.isChanged = true;
  }

  // data files

  fileChange(event, form){
    this.dataFile = event.target.files[0];
    this.uploading = true;

    this.systemsService.postDataFile(this.dataFile).then(res =>{
      this.uploading = false;
      this.data_files.push({
        url: 'file://' + res.text(),
        name: this.dataFile.name
      });
      this.dataFile = null;
      this.isChanged = true;
    }, err =>{
      this.uploading = false;
      this.dataFile = null;
    });
  }

  addDataFile(form): void{
    if (form.valid){
      this.data_files.push({
        url: this.data.url,
        name: this.data.name.trim()
      });
      form.reset();
      this.isChanged = true;
    }
  }

  deleteDataFile(i): void{
    this.data_files.splice(i, 1);
    this.isChanged = true;
  }

  getFileUrl(url){
    if (url.substring(0,7) === 'file://'){
      return '/api/v1/files/' + url.substring(7);
    } else {
      return url;
    }
  }

  saveSystem(){
    this.system.details.stored_data = this.stored_data;
    this.system.details.data_files = this.data_files;
    this.systemsService.updateSystem(this.system).then(response => {
      this.router.navigate(['/Kirjelda/Vaata/', response.json().details.short_name]);
    });
    this.activeModal.close('saved');
  }

  closeModal(f, i){
    if (this.isChanged || f.form.dirty || i.value.length > 0){
      if (confirm('Oled väljades muudatusi teinud. Kui navigeerid siit ära ilma salvestamata, siis sinu muudatused kaovad.')){
        this.activeModal.close();
      } else {
        return false;
      }
    } else {
      this.activeModal.close();
    }
  }

  constructor(private activeModal: NgbActiveModal,
              private systemsService: SystemsService,
              private router: Router) {
  }

  ngOnInit() {
  }

}
