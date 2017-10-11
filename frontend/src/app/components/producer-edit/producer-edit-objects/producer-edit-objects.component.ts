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
  dataFile: any = null;
  dataFileUuid: string = null;
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
    }
  }

  deleteStoredDataObject(i): void {
    this.stored_data.splice(i, 1);
  }

  // data files

  fileChange(event, form){
    this.dataFile = event.target.files[0];
    this.dataFileUuid = null;
    this.uploading = true;

    this.systemsService.postDataFile(this.dataFile).then(res =>{
      this.uploading = false;
      this.dataFileUuid = res.text();
      this.data.name = this.dataFile.name;

    }, err =>{
      this.uploading = false;
      this.dataFile = null;
    });
  }

  private resetDataFile(){
    this.data.url = '';
    this.data.name = '';
    this.dataFileUuid = null;
    this.dataFile = null;
    this.uploading = false;
  }

  resetFileForm(form){
    form.reset();
    this.resetDataFile();
  }

  addDataFile(form): void{
    if (form.valid){
      this.data_files.push({
        url: this.dataFileUuid ? 'file://' + this.dataFileUuid : this.data.url,
        name: this.data.name.trim()
      });
      form.reset();
      this.resetDataFile();
    }
  }

  deleteDataFile(i): void{
    this.data_files.splice(i, 1);
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

  constructor(public activeModal: NgbActiveModal,
              private systemsService: SystemsService,
              private router: Router) {
  }

  ngOnInit() {
  }

}
