import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {SystemsService} from '../../../services/systems.service';
import {System} from '../../../models/system';
import {ToastrService} from 'ngx-toastr';
import {ModalHelperService} from '../../../services/modal-helper.service';
import {GeneralHelperService} from '../../../services/general-helper.service';
import {classifiers} from '../../../services/environment.service';
import {NgForm} from "@angular/forms";
import {Observable} from "rxjs";
import {CanDeactivateModal} from '../../../guards/can-deactivate-modal.guard';
import {CONSTANTS} from '../../../utils/constants';

@Component({
  selector: 'app-producer-edit-tech-docs',
  templateUrl: './producer-edit-documents.component.html',
  styleUrls: ['./producer-edit-documents.component.scss']
})
export class ProducerEditDocumentsComponent implements OnInit, CanDeactivateModal {

  @ViewChild('addForm') formObjectAdd: NgForm;
  @ViewChild('editForm') formObjectEdit: NgForm;

  @Input() system: System;
  documents: any[] = [];
  isChanged: boolean = false;
  classifiers = classifiers;

  docFile: any = null;
  uploading: boolean = false;
  showAddLinkFields: boolean = false;
  showAddFileFields: boolean = false;
  data: any = {url: '', name: '', type:''};
  blocks = [];
  docsButtonClicked: boolean = false;
  uploadButtonClicked: boolean = false;

  addTechDoc(addForm): void {
    if (addForm.valid){
      this.documents.push({url: this.data.url,
                          name: this.data.name ? this.data.name.trim() : '',
                          type: this.data.type});
      this.data = {url: '', name: '', type: ''};
      addForm.reset();
      this.isChanged = true;
      this.showAddLinkFields = false;
      this.docsButtonClicked = false;
    }else {
      this.docsButtonClicked = true;
    }
  }

  fileChange(event){
    this.docFile = event.target.files[0];
  }

  uploadFile(event, addForm){
    if (addForm.valid && this.docFile) {
      this.uploading = true;

      this.systemsService.postDataFile(this.docFile, this.system.details.short_name).subscribe(fileUrl => {
        this.uploading = false;
        this.documents.push({
          url: 'file://' + fileUrl,
          name: this.docFile.name,
          type: this.data.type
        });
        this.data = {url: '', name: '', type: ''};
        this.docFile = null;
        this.isChanged = true;
        this.showAddFileFields = false;
      }, err => {
        this.uploading = false;
        this.docFile = null;
        if (err.status === 0) {
          err.error.message = CONSTANTS.SERVER_ERROR_CHECK_FILE_SIZE;
        }
        this.generalHelperService.showError(err.error.message);
      });
      this.uploadButtonClicked = false;
    }else {
      this.uploadButtonClicked = true;
    }
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
      this.systemsService.getSystem(this.system.details.short_name).subscribe(responseSystem => {
        const system = new System(responseSystem);
        system.details.documents = this.prepareForSaving(this.documents);
        this.systemsService.updateSystem(system).subscribe(updatedSystem => {
          this.modalService.closeActiveModal({system: new System(updatedSystem)});
        }, err => {
          this.toastrService.error('Serveri viga.');
        });
      }, err => {
        this.toastrService.error('Serveri viga.');
      });
    }
  }


  isUploaded(doc){
    return doc.url.substr(0,7) == 'file://';
  }

  clearAccessRestriction(e, i){
    if (e){
      const d = new Date();
      this.documents[i].accessRestriction = {
        startDate: {
          day: d.getDate(),
          month: d.getMonth() + 1, //JS Date object counts month 0 to 11
          year: d.getFullYear()
        },
        endDate: {
          day: d.getDate(),
          month: d.getMonth() + 1, //JS Date object counts month 0 to 11
          year: d.getFullYear() + 5
        },
        reasonCode: '',
        organization: this.system.details.owner
      }
    } else {
      this.documents[i].accessRestriction = null;
    }
  }

  canDeactivate(): Observable<boolean> | Promise<boolean> | boolean {
    return this.closeModal();
  }

  closeModal() {
    if (this.isFormChanged) {
      const observer = this.modalService.confirm(CONSTANTS.CLOSE_DIALOG_WARNING);
      observer.subscribe(confirmed => {
        if (confirmed) {
          this.modalService.dismissActiveModal();
        }
      });
      return observer;
    }

    this.modalService.dismissActiveModal();
    return true;
  }

  /**
   * Getters
   */

  /**
   * Is form data changed ?
   */
  get isFormChanged(): boolean {
    return this.isChanged || this.formObjectAdd.form.dirty || this.formObjectEdit.form.dirty
  }

  constructor(private modalService: ModalHelperService,
              private systemsService: SystemsService,
              private toastrService: ToastrService,
              public generalHelperService: GeneralHelperService) { }

  ngOnInit() {
    const system = this.generalHelperService.cloneObject(this.system);
    this.documents = this.prepareForDisplay(system.details.documents || []);
  }

}
