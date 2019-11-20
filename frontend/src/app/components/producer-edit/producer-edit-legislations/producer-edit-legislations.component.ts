import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {SystemsService} from '../../../services/systems.service';
import {System} from '../../../models/system';
import {ToastrService} from 'ngx-toastr';
import {ModalHelperService} from '../../../services/modal-helper.service';
import {GeneralHelperService} from '../../../services/general-helper.service';
import {classifiers} from "../../../services/environment.service";
import {Observable} from "rxjs";
import {NgForm} from "@angular/forms";
import {CanDeactivateModal} from '../../../guards/can-deactivate-modal.guard';
import {CONSTANTS} from '../../../utils/constants';

@Component({
  selector: 'app-producer-edit-legislations',
  templateUrl: './producer-edit-legislations.component.html',
  styleUrls: ['./producer-edit-legislations.component.scss']
})
export class ProducerEditLegislationsComponent implements OnInit, CanDeactivateModal {

  @ViewChild('addForm', null) formObjectAdd: NgForm;

  @Input() system: System;
  legislations: any[] = [];
  isChanged: boolean = false;
  classifiers = classifiers;

  data: any = {url: '', name: '', type: ''};

  addLegislation(addForm): void {
    if (addForm.valid){
      this.legislations.push({url: this.data.url,
                              name: this.data.name ? this.data.name.trim() : '',
                              type: this.data.type});
      this.data = {url: '', name: '', type: ''};
      addForm.reset();
      this.isChanged = true;
    }
  }

  deleteLegislation(i): void {
    this.legislations.splice(i, 1);
    this.isChanged = true;
  }

  saveSystem(){
    this.systemsService.getSystem(this.system.details.short_name).subscribe(responseSystem => {
      const system = new System(responseSystem);
      system.details.legislations = this.legislations;
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
    return this.isChanged || this.formObjectAdd.form.dirty
  }

  constructor(private modalService: ModalHelperService,
              private systemsService: SystemsService,
              private toastrService: ToastrService,
              public generalHelperService: GeneralHelperService) { }

  ngOnInit() {
    const system = this.generalHelperService.cloneObject(this.system);
    this.legislations = system.details.legislations || [];
  }

}
