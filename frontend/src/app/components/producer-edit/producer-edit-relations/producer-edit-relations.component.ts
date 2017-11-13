import { Component, OnInit, Input } from '@angular/core';
import { SystemsService } from '../../../services/systems.service';
import { G } from '../../../globals/globals';
import { System } from '../../../models/system';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-producer-edit-relations',
  templateUrl: './producer-edit-relations.component.html',
  styleUrls: ['./producer-edit-relations.component.scss']
})
export class ProducerEditRelationsComponent implements OnInit {

  @Input() system: System;
  @Input() relations: any[];

  globals: any = G;
  relation: {
    infoSystemShortName: string,
    type: string
  };

  addRelation(addForm){
    if (addForm.valid){
      this.systemsService.addSystemRelation(this.system.details.short_name, this.relation).then(res => {
        this.refreshRelations();
        addForm.reset();
        addForm.controls.type.setValue(this.globals.relation_type.SUB_SYSTEM);
      }, err => {
        this.toastrService.error('Serveri viga');
      });
    }
  };

  deleteRelation(id){
    this.systemsService.deleteSystemRelation(this.system.details.short_name, id).then(res => {
      this.refreshRelations();
    }, err => {
      this.toastrService.error('Serveri viga');
    });
  };

  refreshRelations(){
    this.systemsService.getSystemRelations(this.system.details.short_name).then(
      res => {
        this.relations = res.json();
      }
    )
  }

  closeModal(){
    this.activeModal.close();
  };

  constructor(private systemsService: SystemsService,
              private toastrService: ToastrService,
              private activeModal: NgbActiveModal) { }

  ngOnInit() {
    this.relation = {
      infoSystemShortName: null,
      type: this.globals.relation_type.SUB_SYSTEM
    };
  }

}
