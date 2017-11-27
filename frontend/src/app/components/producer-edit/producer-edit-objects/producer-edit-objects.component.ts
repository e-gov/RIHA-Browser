import { Component, OnInit, Input } from '@angular/core';
import { SystemsService } from '../../../services/systems.service';
import { GeneralHelperService } from '../../../services/general-helper.service';
import { System } from '../../../models/system';
import { ToastrService } from 'ngx-toastr';
import { ModalHelperService } from '../../../services/modal-helper.service';

@Component({
  selector: 'app-producer-edit-objects',
  templateUrl: './producer-edit-objects.component.html',
  styleUrls: ['./producer-edit-objects.component.scss']
})
export class ProducerEditObjectsComponent implements OnInit {

  @Input() system: System;
  stored_data: string[] =[];
  data_files: any[] = [];
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

  saveSystem(){
    this.systemsService.getSystem(this.system.details.short_name).then(res =>{
      let s = res.json();
      s.details.stored_data = this.stored_data;
      s.details.data_files = this.data_files;
      this.systemsService.updateSystem(s).then(response => {
        this.modalService.closeActiveModal({system: new System(response.json())});
      }, err => {
        this.toastrService.error('Serveri viga.');
      });
    }, err => {
      this.toastrService.error('Serveri viga.');
    });
  }

  closeModal(f, i){
    if (this.isChanged || f.form.dirty || i.value.length > 0){
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
              public generalHelperService: GeneralHelperService,
              private toastrService: ToastrService) {
  }

  ngOnInit() {
    let system = this.generalHelperService.cloneObject(this.system);
    this.stored_data = system.details.stored_data || [];
    this.data_files = system.details.data_files || [];
  }

}
