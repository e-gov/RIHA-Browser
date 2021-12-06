import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {SystemsService} from '../../../services/systems.service';
import {GeneralHelperService} from '../../../services/general-helper.service';
import {System} from '../../../models/system';
import {ToastrService} from 'ngx-toastr';
import {ModalHelperService} from '../../../services/modal-helper.service';
import {NgForm} from "@angular/forms";
import {Observable} from "rxjs";
import {CanDeactivateModal} from '../../../guards/can-deactivate-modal.guard';
import {CONSTANTS} from '../../../utils/constants';

@Component({
  selector: 'app-producer-edit-objects',
  templateUrl: './producer-edit-objects.component.html',
  styleUrls: ['./producer-edit-objects.component.scss']
})
export class ProducerEditObjectsComponent implements OnInit, CanDeactivateModal {

  @ViewChild('dataFilesForm') formObject: NgForm;
  @ViewChild('object') inputObject: ElementRef;

  @Input() system: System;
  stored_data: string[] =[];
  data_files: any[] = [];
  isChanged: boolean = false;
  buttonClicked: boolean = false;

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

    this.systemsService.postDataFile(this.dataFile, this.system.details.short_name).subscribe(fileUrl => {
      this.uploading = false;
      this.data_files.push({
        url: 'file://' + fileUrl,
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
      this.buttonClicked = false;
    }else {
      this.buttonClicked = true;
    }
  }

  deleteDataFile(i): void{
    this.data_files.splice(i, 1);
    this.isChanged = true;
  }

  saveSystem() {
    this.systemsService.getSystem(this.system.details.short_name).subscribe(responseSystem => {
      const system = new System(responseSystem);
      system.details.stored_data = this.stored_data;
      system.details.data_files = this.data_files;
      this.systemsService.updateSystem(system).subscribe(updatedSystem => {
        this.modalService.closeActiveModal({system: new System(updatedSystem)});
      }, err => {
        this.toastrService.error('Serveri viga.');
      });
    }, err => {
      this.toastrService.error('Serveri viga.');
    });
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
    return this.isChanged || this.formObject.form.dirty || this.inputObject.nativeElement.value.length > 0
  }

  constructor(private modalService: ModalHelperService,
              private systemsService: SystemsService,
              public generalHelperService: GeneralHelperService,
              private toastrService: ToastrService) {
  }

  ngOnInit() {
    const system = this.generalHelperService.cloneObject(this.system);
    this.stored_data = system.details.stored_data || [];
    this.data_files = system.details.data_files || [];
  }
}
