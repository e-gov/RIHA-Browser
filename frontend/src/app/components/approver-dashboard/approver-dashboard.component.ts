import { Component, OnInit, HostListener } from '@angular/core';
import { GeneralHelperService } from '../../services/general-helper.service';
import { EnvironmentService } from '../../services/environment.service';
import {ModalHelperService} from "../../services/modal-helper.service";
import {ActiveOrganizationChooserComponent} from '../active-organization-chooser/active-organization-chooser.component';

@Component({
  selector: 'app-approver-dashboard',
  templateUrl: './approver-dashboard.component.html',
  styleUrls: ['./approver-dashboard.component.scss']
})
export class ApproverDashboardComponent implements OnInit {

  openOrganizationsModal() {
    const modalRef = this.modalService.open(ActiveOrganizationChooserComponent);
    return false;
  }
  
  constructor(public helper: GeneralHelperService,
              public generalHelperService: GeneralHelperService,
              public environmentService: EnvironmentService,
              private modalService: ModalHelperService) {
    this.userMatrix = this.environmentService.getUserMatrix();
  }

  ngOnInit() {
    this.generalHelperService.setRihaPageTitle('Hinda');
  }

  @HostListener("window:scroll", [])
  onWindowScroll() {

  }

}
