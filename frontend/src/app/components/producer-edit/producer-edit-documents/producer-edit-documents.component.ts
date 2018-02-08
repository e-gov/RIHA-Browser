import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { SystemsService } from '../../../services/systems.service';
import { System } from '../../../models/system';
import { ToastrService } from 'ngx-toastr';
import { ModalHelperService } from '../../../services/modal-helper.service';
import { GeneralHelperService } from '../../../services/general-helper.service';
import { G } from '../../../globals/globals';
import { EnvironmentService } from '../../../services/environment.service';

@Component({
  selector: 'app-producer-edit-tech-docs',
  templateUrl: './producer-edit-documents.component.html',
  styleUrls: ['./producer-edit-documents.component.scss']
})
export class ProducerEditDocumentsComponent implements OnInit {

  @Input() system: System;
  documents: any[] = [];
  isChanged: boolean = false;
  globals: any = G;

  docFile: any = null;
  uploading: boolean = false;
  showAddLinkFields: boolean = false;
  data: any = {url: '', name: ''};
  blocks = [];

  addTechDoc(addForm): void {
    if (addForm.valid){
      this.documents.push({url: this.data.url,
                          name: this.data.name ? this.data.name.trim() : ''});
      this.data = {url: '', name: ''};
      addForm.reset();
      this.isChanged = true;
      this.showAddLinkFields = false;
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

  private prepareForSaving(docs){
    if (docs){
      for (let i = 0; i < docs.length; i++){
        if (docs[i].accessRestriction){
          docs[i].accessRestriction.startDate = this.generalHelperService.dateObjToTimestamp(docs[i].accessRestriction.startDate, true);
          docs[i].accessRestriction.endDate = this.generalHelperService.dateObjToTimestamp(docs[i].accessRestriction.endDate, true);
        }
      }
    }
    return docs;
  }

  private prepareForDisplay(docs){
    if (docs){
      for (let i = 0; i < docs.length; i++){
        if (docs[i].accessRestriction){
          docs[i].accessRestriction.startDate = this.generalHelperService.timestampToDateObj(docs[i].accessRestriction.startDate);
          docs[i].accessRestriction.endDate = this.generalHelperService.timestampToDateObj(docs[i].accessRestriction.endDate);
        }
      }
    }
    return docs;
  }

  saveSystem(editForm){
    if (editForm.valid){
      this.systemsService.getSystem(this.system.details.short_name).then(res =>{
        let s = new System(res.json());
        s.details.documents = this.prepareForSaving(this.documents);
        this.systemsService.updateSystem(s).then(response => {
          this.modalService.closeActiveModal({system: new System(response.json())});
        }, err => {
          this.toastrService.error('Serveri viga.');
        });
      }, err => {
        this.toastrService.error('Serveri viga.');
      });
    }
  }

  closeModal(addForm, editForm){
    if (this.isChanged || addForm.form.dirty || editForm.form.dirty){
      if (confirm('Oled väljades muudatusi teinud. Kui navigeerid siit ära ilma salvestamata, siis sinu muudatused kaovad.')){
        this.modalService.dismissActiveModal();
      } else {
        return false;
      }
    } else {
      this.modalService.dismissActiveModal();
    }
  }

  isUploaded(doc){
    return doc.url.substr(0,7) == 'file://';
  }

  clearAccessRestriction(e, i){
    if (e){
      this.documents[i].accessRestriction = {
        startDate: null,
        endDate: null,
        reasonCode: '',
        organization: this.system.details.owner
      }
    } else {
      this.documents[i].accessRestriction = null;
    }
  }

  constructor(private modalService: ModalHelperService,
              private systemsService: SystemsService,
              private toastrService: ToastrService,
              private environmentService: EnvironmentService,
              private generalHelperService: GeneralHelperService) { }

  ngOnInit() {
    let system = this.generalHelperService.cloneObject(this.system);
    this.documents = this.prepareForDisplay(system.details.documents || []);
  }

}
