import { Component, Input, OnInit } from '@angular/core';
import { SystemsService } from '../../../services/systems.service';
import { System } from '../../../models/system';
import { G } from '../../../globals/globals';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ProducerEditRelationsComponent } from '../../producer-edit/producer-edit-relations/producer-edit-relations.component';
import { Router } from '@angular/router';
import { WindowRefService } from '../../../services/window-ref.service';

@Component({
  selector: 'app-producer-details-relations',
  templateUrl: './producer-details-relations.component.html',
  styleUrls: ['./producer-details-relations.component.scss']
})
export class ProducerDetailsRelationsComponent implements OnInit {

  @Input() system: System;
  @Input() allowEdit: boolean;

  globals: any = G;
  relations: any[] = [];

  openSystemDetails(shortName){
    this.winRef.nativeWindow.scrollTo(0, 0);
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
      keyboard: false
    });
    modalRef.componentInstance.system = this.system;
    modalRef.componentInstance.relations = this.relations;
    modalRef.result.then(res => {
      this.refreshRelations();
    }, err => {

    });
  };

  constructor(private systemsService: SystemsService,
              private winRef: WindowRefService,
              private router: Router,
              private modalService: NgbModal) { }

  ngOnInit() {
    this.refreshRelations();
  }

}
