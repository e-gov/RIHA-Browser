import {Component, Input, KeyValueDiffers, OnInit} from '@angular/core';
import {SystemsService} from '../../../services/systems.service';
import {System} from '../../../models/system';
import {G} from '../../../globals/globals';
import {ModalHelperService} from '../../../services/modal-helper.service';
import {ProducerEditRelationsComponent} from '../../producer-edit/producer-edit-relations/producer-edit-relations.component';
import {Router} from '@angular/router';
import {ProducerEditStandardRealisationsComponent} from '../../producer-edit/producer-edit-standard-realisations/producer-edit-standard-realisations.component';
import {EnvironmentService} from '../../../services/environment.service';
import {UserMatrix} from '../../../models/user-matrix';

@Component({
  selector: 'app-producer-details-relations',
  templateUrl: './producer-details-relations.component.html',
  styleUrls: ['./producer-details-relations.component.scss']
})
export class ProducerDetailsRelationsComponent implements OnInit    {

  @Input() system: System;
  @Input() allowEdit: boolean;

  @Input() userMatrix: UserMatrix;
  globals: any = G;
  relations: any[] = [];

  openSystemDetails(shortName){
    this.router.navigate(['/Infosüsteemid/Vaata', shortName]);
    return false;
  }

  refreshRelations(){
    this.systemsService.getSystemRelations(this.system.details.short_name).then(
      res => {
        this.relations = res.json();
      }
    )
  }

  openRelationsEdit(){
    const modalRef = this.modalService.open(ProducerEditRelationsComponent, {
      size: "lg",
      backdrop: "static",
      windowClass: "fixed-header-modal",
      keyboard: false
    });
    modalRef.componentInstance.system = this.system;
    modalRef.componentInstance.relations = this.relations;
    modalRef.result.then(res => {
      this.refreshRelations();
    }, err => {

    });
  };

  openStandardUserInfosystemModal() {
    const modalRef = this.modalService.open(ProducerEditStandardRealisationsComponent, {
      size: "lg",
      backdrop: "static",
      windowClass: "fixed-header-modal",
      keyboard: false
    });
    modalRef.componentInstance.system = this.system;
  }

  constructor(private differs: KeyValueDiffers,
              private systemsService: SystemsService,
              private environmentService: EnvironmentService,
              private router: Router,
              private modalService: ModalHelperService) {

  }

  ngOnInit() {
    this.refreshRelations();
  }

}
