import { Component, OnInit, Input } from '@angular/core';
import { SystemsService } from '../../../services/systems.service';
import { System } from '../../../models/system';
import { ToastrService } from 'ngx-toastr';
import { GeneralHelperService } from '../../../services/general-helper.service';
import { ModalHelperService } from '../../../services/modal-helper.service';

@Component({
  selector: 'app-producer-edit-tech-docs',
  templateUrl: './producer-edit-documents.component.html',
  styleUrls: ['./producer-edit-documents.component.scss']
})
export class ProducerEditDocumentsComponent implements OnInit {

  @Input() system: System;
  documents: any[];
  isChanged: boolean = false;

  loaded: boolean = false;
  docFile: any = null;
  uploading: boolean = false;
  data: any = {url: '', name: ''};

  addTechDoc(addForm): void {
    if (addForm.valid){
      this.documents.push({url: this.data.url,
                          name: this.data.name ? this.data.name.trim() : ''});
      this.data = {url: '', name: ''};
      addForm.reset();
      this.isChanged = true;
    }
  }

  fileChange(event, form){
    this.docFile = event.target.files[0];
    this.uploading = true;

    this.systemsService.postDataFile(this.docFile).then(res =>{
      this.uploading = false;
      this.documents.push({
        url: 'file://' + res.text(),
        name: this.docFile.name
      });
      this.docFile = null;
      this.isChanged = true;
    }, err =>{
      this.uploading = false;
      this.docFile = null;
    });
  }

  deleteTechDoc(i): void {
    this.documents.splice(i, 1);
    this.isChanged = true;
  }

  saveSystem(){
    this.systemsService.getSystem(this.system.details.short_name).then(res =>{
      let s = res.json();
      s.details.documents = this.documents;
      this.systemsService.updateSystem(s).then(response => {
        this.modalService.closeActiveModal({system: new System(response.json())});
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
    this.systemsService.getSystem(this.system.details.short_name).then(
      res => {
        let system = res.json();
        this.documents = system.details.documents;
        this.loaded = true;
      }, err => {
        this.toastrService.error('Serveri viga.');
        this.modalService.dismissAllModals();
      }
    )
  }

}
