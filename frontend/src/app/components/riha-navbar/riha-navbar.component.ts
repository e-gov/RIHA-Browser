import { Component, OnInit } from '@angular/core';
import { JsonDataService } from '../../json-data.service';
import { EnvironmentService } from '../../services/environment.service';
import { User } from '../../models/user';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActiveOrganizationChooserComponent } from '../active-organization-chooser/active-organization-chooser.component';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';

declare var $: any;

@Component({
  selector: 'app-riha-navbar',
  templateUrl: './riha-navbar.component.html',
  styleUrls: ['./riha-navbar.component.scss']
})
export class RihaNavbarComponent implements OnInit {
  private routes = [];
  public activeUser: User = null;

  isUserLoggedIn(): boolean {
    return this.environmentService.getActiveUser() != null;
  }

  doLogout(){
    this.environmentService.setActiveUser(null);
  }

  openOrganizationsModal() {
    const modalRef = this.modalService.open(ActiveOrganizationChooserComponent);
    return false;
  }

  isAllowedToChangeOrganization(): boolean {
    let user = this.environmentService.getActiveUser();
    return user.organizations.length > 1;
  }

  getUserText(): string {
    let user = this.environmentService.getActiveUser();
    return user.getFullNameWithActiveOrganization();
  }

  constructor(private jsonDataService: JsonDataService,
              private environmentService: EnvironmentService,
              private modalService: NgbModal) {
    jsonDataService.routes.subscribe(this.updateRoutes.bind(this));
  }

  updateRoutes(routes) {
    this.routes = routes;
  }

  ngOnInit() {
  }

  openNavigationMenu() {
    $('body').toggleClass('nav-open');
  }

}
