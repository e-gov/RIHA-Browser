import { Component, OnInit } from '@angular/core';
import { EnvironmentService } from '../../services/environment.service';
import { User } from '../../models/user';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActiveOrganizationChooserComponent } from '../active-organization-chooser/active-organization-chooser.component';
import { Http } from '@angular/http';
import { Router } from '@angular/router';

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

  constructor(private environmentService: EnvironmentService,
              private modalService: NgbModal,
              private http: Http,
              private router: Router) {
  }

  ngOnInit() {
  }

  openNavigationMenu() {
    $('body').toggleClass('nav-open');
  }

}
