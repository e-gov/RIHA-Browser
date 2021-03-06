import {AfterContentChecked, Component, OnInit} from '@angular/core';
import {EnvironmentService} from '../../services/environment.service';
import {User} from '../../models/user';
import {ModalHelperService} from '../../services/modal-helper.service';
import {ActiveOrganizationChooserComponent} from '../active-organization-chooser/active-organization-chooser.component';
import {Router} from '@angular/router';
import {SessionHelperService} from '../../services/session-helper.service';
import {NoOrganizationModalComponent} from '../no-organization-modal/no-organization-modal.component';
import * as $ from 'jquery';

@Component({
  selector: 'app-riha-navbar',
  templateUrl: './riha-navbar.component.html',
  styleUrls: ['./riha-navbar.component.scss']
})
export class RihaNavbarComponent implements OnInit, AfterContentChecked {
  public activeUser: User = null;
  public _dc: number = 0;

  isUserLoggedIn(): boolean {
    return typeof this.environmentService.getActiveUser() !== 'undefined' && this.environmentService.getActiveUser() != null;
  }

  getRand() {
    return new Date().getSeconds();
  }

  getCurrentUrl() {
    return this.router.url;
  }

  /*
   * TODO: replace with routerLinkActive
   * in version 4.1.3 it seems to be broken when working with queryParams,
   * even with applied [routerLinkActiveOptions]="{exact: false}
   */
  isListOrSubView() {
    const cat = encodeURI('/Infosüsteemid?');
    const obj = encodeURI('/Andmeobjektid');
    const sub = encodeURI('/Infosüsteemid/Vaata');
    const full = encodeURI('/Infosüsteemid');
    if (this.router.url && typeof this.router.url === 'string') {
      return (-1 != this.router.url.indexOf(cat) || -1 != this.router.url.indexOf(obj) || -1 != this.router.url.indexOf(sub) || this.router.url === full);
    } else {
      return false;
    }
  }

  logout() {
    this.environmentService.doLogout().subscribe(res => {
      this.environmentService.loadEnvironmentData().subscribe(env => {
        this.sessionHelperService.refreshSessionTimer();
        this.router.navigate(['/Tagasiside/logout']);
      });
    });
  }

  openOrganizationsModal() {
    const modalRef = this.modalService.open(ActiveOrganizationChooserComponent);
    return false;
  }

  openNoOrganizationWarningModal(): boolean {
    const modalRef = this.modalService.open(NoOrganizationModalComponent);
    return false;
  }

  isAllowedToChangeOrganization(): boolean {
    const user = this.environmentService.getActiveUser();
    return user && user.getOrganizations().length > 1 || (user.getOrganizations().length == 1 && user.getActiveOrganization() == null);
  }

  isNoOrganizationPresent(): boolean {
    const user = this.environmentService.getActiveUser();
    return user && user.getOrganizations().length == 0;
  }

  noOrganizationSelected(): boolean {
    const user = this.environmentService.getActiveUser();
    return user && user.getActiveOrganization() == null;
  }

  getUserText(): string {
    const user = this.environmentService.getActiveUser();
    return user && user.getFullNameWithActiveOrganization();
  }

  searchInfosystems(input) {
    this.router.navigate(['/Infosüsteemid'], {
      queryParams: {
        searchText: input.value
      }
    });
    input.value = '';
  }

  constructor(public environmentService: EnvironmentService,
              private modalService: ModalHelperService,
              private sessionHelperService: SessionHelperService,
              private router: Router) {
  }

  ngOnInit() {
  }

  ngAfterContentChecked() {
    this._dc = this.getRand();
  }

  openNavigationMenu() {
    $('body').toggleClass('nav-open');
  }

}
