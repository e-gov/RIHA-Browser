import {Component, Input, OnInit} from '@angular/core';
import {System} from '../../../models/system';
import {classifiers} from "../../../services/environment.service";
import {Router} from '@angular/router';
import {GeneralHelperService} from '../../../services/general-helper.service';
import {
  ProducerEditStandardRealisationsComponent
} from "../../producer-edit/producer-edit-standard-realisations/producer-edit-standard-realisations.component";
import {ModalHelperService} from "../../../services/modal-helper.service";
import {UserMatrix} from "../../../models/user-matrix";

@Component({
  selector: 'app-producer-details-general',
  templateUrl: './producer-details-general.component.html',
  styleUrls: ['./producer-details-general.component.scss']
})
export class ProducerDetailsGeneralComponent implements OnInit {

  @Input() system: System;
  @Input() allowEdit: boolean;
  @Input() userMatrix: UserMatrix;
  classifiers = classifiers;

  listSystemsWithTopic(topic){
    this.router.navigate(['/Infosüsteemid'], {queryParams: {topic: topic}});
  }

  openStandardUserInfosystemModal() {
    const modalRef = this.modalService.open(ProducerEditStandardRealisationsComponent, {
      size: "lg",
      backdrop: "static",
      windowClass: "fixed-header-modal",
      keyboard: false
    });
    modalRef.componentInstance.system = this.system;
  }

  constructor(private router: Router,  private helper: GeneralHelperService, private modalService: ModalHelperService) { }

  ngOnInit() {
  }

}
