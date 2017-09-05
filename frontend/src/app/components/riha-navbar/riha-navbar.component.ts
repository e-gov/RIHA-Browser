import { Component, OnInit } from '@angular/core';
import { JsonDataService } from '../../json-data.service';
import { EnvironmentService } from '../../services/environment.service';
import { User } from '../../models/user';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActiveOrganizationChooserComponent } from '../active-organization-chooser/active-organization-chooser.component';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { Http } from '@angular/http';
import { Router } from '@angular/router';

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
    this.http.get('/logout').toPromise().then(res => {
      this.environmentService.load().then(env => {
        this.router.navigate(['/']);
      });
    });
  }

  openOrganizationsModal() {
    const modalRef = this.modalService.open(ActiveOrganizationChooserComponent);
    return false;
  }

  isAllowedToChangeOrganization(): boolean {
    /*let user = this.environmentService.getActiveUser();
    return user.organizations.length > 1;*/

    // organization changing disabled until backend support is available
    return false;
  }

  getUserText(): string {
    let user = this.environmentService.getActiveUser();
    return user.getFullNameWithActiveOrganization();
  }

  constructor(private jsonDataService: JsonDataService,
              private environmentService: EnvironmentService,
              private modalService: NgbModal,
              private http: Http,
              private router: Router) {
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
