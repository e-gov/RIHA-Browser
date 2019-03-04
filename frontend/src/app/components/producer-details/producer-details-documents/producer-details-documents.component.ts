import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ModalHelperService } from '../../../services/modal-helper.service';
import { ProducerEditDocumentsComponent } from '../../producer-edit/producer-edit-documents/producer-edit-documents.component';
import { System } from '../../../models/system';
import { GeneralHelperService } from '../../../services/general-helper.service';
import { SystemsService } from '../../../services/systems.service';
import { ToastrService } from 'ngx-toastr';
import { EnvironmentService } from '../../../services/environment.service';
import { GridData } from "../../../models/grid-data";
import { classifiers } from '../../../services/environment.service';
import _ from 'lodash';

@Component({
  selector: 'app-producer-details-tech-docs',
  templateUrl: './producer-details-documents.component.html',
  styleUrls: ['./producer-details-documents.component.scss']
})
export class ProducerDetailsDocumentsComponent implements OnInit {

  @Input() system: System;
  @Input() allowEdit: boolean;
  @Output() onSystemChanged = new EventEmitter<System>();

  gridData: GridData = new GridData();
  classifiers = classifiers;

  onSortChange(property): void{
    this.gridData.changeSortOrder(property);
    this.getDocuments();
  }

  canDownload(doc){
    if (!doc.accessRestriction){
      return true;
    } else {
      return this.allowEdit || this.environmentService.getUserMatrix().hasApproverRole;
    }
  }

  openTechDocsEdit(content) {
    this.systemsService.getSystem(this.system.details.short_name).then( res => {
      let system = new System(res.json());
      this.onSystemChanged.emit(system);
      const modalRef = this.modalService.open(ProducerEditDocumentsComponent,{
        backdrop: 'static',
        size: 'lg',
        windowClass: 'fixed-header-modal',
        keyboard: false
      });
      modalRef.componentInstance.system = system;
      modalRef.result.then( result => {
        if (result.system) {
          this.onSystemChanged.emit(result.system);
          this.system = result.system;
          this.getDocuments();
        }
      }, reason => {

      });
    }, err => {
      this.toastrService.error('Serveri viga.');
    });
  }

  constructor(private modalService: ModalHelperService,
              public generalHelperService: GeneralHelperService,
              private environmentService: EnvironmentService,
              private systemsService: SystemsService,
              private toastrService: ToastrService) { }

  ngOnInit() {
    this.gridData.changeSortOrder('name');
    this.getDocuments();
  }

  getDocuments() {
    let documents = this.system.details.documents;
    if (documents && documents.constructor === Array && documents.length > 0) {
      let sort = this.gridData.sort;
      let sortOrder = 'asc';
      if(sort[0] === "-") {
        sortOrder = 'desc';
        sort = sort.substr(1);
      }

      documents = documents.map(doc => {
        if (!doc.update_timestamp) {
          doc.update_timestamp = doc.creation_timestamp;
        }
        if (doc.type && this.classifiers.document_types[doc.type]) {
          doc.typeForSorting = this.classifiers.document_types[doc.type].value;
        } else {
          doc.typeForSorting = '';
        }
        return doc;
      });
      documents = _.orderBy(documents, [sort, 'name'], [sortOrder, 'asc']);
    }

    this.gridData.updateData({content: documents});
  }
}
