import { Component, OnInit } from '@angular/core';
import { EnvironmentService } from '../../services/environment.service';
import { User } from '../../models/user';
import { ModalHelperService } from '../../services/modal-helper.service';
import { ActiveOrganizationChooserComponent } from '../active-organization-chooser/active-organization-chooser.component';
import { Router } from '@angular/router';
import { SessionHelperService } from '../../services/session-helper.service';

declare var $: any;

@Component({
  selector: 'app-riha-navbar',
  templateUrl: './riha-navbar.component.html',
  styleUrls: ['./riha-navbar.component.scss']
})
export class RihaNavbarComponent implements OnInit {
  public activeUser: User = null;

  isUserLoggedIn(): boolean {
    return this.environmentService.getActiveUser() != null;
  }

  logout(){
    this.environmentService.doLogout().then(res => {
      this.environmentService.loadEnvironmentData().then(env => {
        this.sessionHelperService.refreshSessionTimer();
        this.router.navigate(['/']);
      });
    });
  }

  openOrganizationsModal() {
    const modalRef = this.modalService.open(ActiveOrganizationChooserComponent);
    return false;
  }

  isAllowedToChangeOrganization(): boolean {
    let user = this.environmentService.getActiveUser();
    return user.getOrganizations().length > 1 || (user.getOrganizations().length == 1 && user.getActiveOrganization() == null);
  }

  noOrganizationSelected(): boolean {
    let user = this.environmentService.getActiveUser();
    return user.getActiveOrganization() == null;
  }

  getUserText(): string {
    let user = this.environmentService.getActiveUser();
    return user.getFullNameWithActiveOrganization();
  }

  searchInfosystems(input) {
    this.router.navigate(['/Infosüsteemid'], {
      queryParams: {
        searchText: input.value
      }
    });
    input.value = '';
  }

  constructor(private environmentService: EnvironmentService,
              private modalService: ModalHelperService,
              private sessionHelperService: SessionHelperService,
              private router: Router) {
  }

  ngOnInit() {
  }

  openNavigationMenu() {
    $('body').toggleClass('nav-open');
  }

}
