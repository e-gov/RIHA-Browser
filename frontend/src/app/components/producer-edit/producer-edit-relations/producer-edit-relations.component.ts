import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {SystemsService} from '../../../services/systems.service';
import {classifiers} from "../../../services/environment.service";
import {System} from '../../../models/system';
import {ToastrService} from 'ngx-toastr';
import {GeneralHelperService} from '../../../services/general-helper.service';
import {ModalHelperService} from '../../../services/modal-helper.service';

import {Observable} from 'rxjs';
import {debounceTime, distinctUntilChanged, switchMap} from 'rxjs/operators';
import {NgForm} from "@angular/forms";
import {CanDeactivateModal} from '../../../guards/can-deactivate-modal.guard';
import {CONSTANTS} from '../../../utils/constants';

@Component({
  selector: 'app-producer-edit-relations',
  templateUrl: './producer-edit-relations.component.html',
  styleUrls: ['./producer-edit-relations.component.scss']
})
export class ProducerEditRelationsComponent implements OnInit, CanDeactivateModal {

  @ViewChild('addForm') formObjectAdd: NgForm;

  @Input() system: System;
  @Input() relations: any[];

  classifiers = classifiers;
  relation: {
    infoSystem: any,
    type: string
  };


  dropDownFormatter = (v)=> {
    return `${v.details.short_name} - ${this.generalHelperService.truncateString(v.details.name, 90)}`;
  };

  inputFormatter = (v)=> v.details.short_name;


  search = (text$: Observable<string>) => {
    return text$
      .pipe(debounceTime(800),
        distinctUntilChanged(),
        switchMap(term => term.length < 2 ? []
        : this.systemsService.getSystemsForAutocomplete(term, this.system.details.short_name))
    );
  };


  addRelation(addForm){
    if (addForm.valid){
      const infoSystemShortName = typeof this.relation.infoSystem === 'string' ? this.relation.infoSystem : this.relation.infoSystem.details.short_name;
      this.systemsService.addSystemRelation(this.system.details.short_name, {infoSystemShortName: infoSystemShortName,
                                                                                     type: this.relation.type}).subscribe(res => {
        this.refreshRelations();
        addForm.reset();
        addForm.controls.type.setValue(this.classifiers.relation_type.SUB_SYSTEM.code);
      }, err => {
        this.toastrService.error('Serveri viga');
      });
    }
  };

  compareClassifiers(first, second): boolean {
    return this.generalHelperService.compareClassifiers(first, second);
  }

  deleteRelation(id){
    this.systemsService.deleteSystemRelation(this.system.details.short_name, id).subscribe(res => {
      this.refreshRelations();
    }, err => {
      this.toastrService.error('Serveri viga');
    });
  };

  refreshRelations(){
    this.systemsService.getSystemRelations(this.system.details.short_name).subscribe(
      relations => {
        this.relations = relations;
      }
    )
  }

  canDeactivate(): Observable<boolean> | Promise<boolean> | boolean {
    return this.closeModal();
  }

  closeModal() {
    if (this.isFormChanged) {
      const observer = this.modalService.confirm(CONSTANTS.CLOSE_DIALOG_WARNING);
      observer.subscribe(confirmed => {
        if (confirmed) {
          this.modalService.closeActiveModal();
        }
      });
      return observer;
    }

    this.modalService.closeActiveModal();
    return true;
  }

  /**
   * Getters
   */

  /**
   * Is form data changed ?
   */
  get isFormChanged(): boolean {
    return this.formObjectAdd.form.dirty
  }

  constructor(private systemsService: SystemsService,
              private modalService: ModalHelperService,
              public generalHelperService: GeneralHelperService,
              private toastrService: ToastrService) { }

  ngOnInit() {
    this.relation = {
      infoSystem: null,
      type: this.classifiers.relation_type.SUB_SYSTEM.code
    };
  }

}
