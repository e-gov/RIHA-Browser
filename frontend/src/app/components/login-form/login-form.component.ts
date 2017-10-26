import { Component, OnInit } from '@angular/core';
import { Http, URLSearchParams } from '@angular/http';
import { ActiveOrganizationChooserComponent } from '../active-organization-chooser/active-organization-chooser.component';
import 'rxjs/add/operator/toPromise';
import { EnvironmentService } from '../../services/environment.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { Environment } from '../../models/environment';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent implements OnInit {

  alertConf: any = null;

  login(){
    this.environmentService.doLogin().then(res => {
      this.environmentService.loadEnvironmentData().then(res => {
        this.router.navigate(['/']);
        let user = this.environmentService.getActiveUser();
        let organizations = user.getOrganizations();
        if (organizations.length > 1){
          this.modalService.open(ActiveOrganizationChooserComponent);
        } else if (organizations.length == 1){
          this.environmentService.setActiveOrganization(organizations[0].code).then(
            res => {
              this.environmentService.globalEnvironment = new Environment(res.json())
            }, err => {}
          );
        }
      });
    }, err => {
      this.alertConf = {
        type: 'danger',
        heading: 'Viga',
        text: 'Viga sisse logimisel'
      }
      setTimeout(()=> this.alertConf = null, 5000)
    });
    return false;
  }

  isLoggedIn(){
    return this.environmentService.getActiveUser() != null;
  }

  constructor(private http: Http,
              private environmentService: EnvironmentService,
              private modalService: NgbModal,
              private router: Router) { }

  ngOnInit() {
  }

}
