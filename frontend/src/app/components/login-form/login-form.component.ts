import { Component, OnInit } from '@angular/core';
import { Http, URLSearchParams  } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { EnvironmentService } from '../../services/environment.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent implements OnInit {

  alertConf: any = null;

  doLogin(){
    this.http.get('/login/esteid').toPromise().then(res => {
      this.environmentService.load().then(res => {
        this.router.navigate(['/']);
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
              private router: Router) { }

  ngOnInit() {
  }

}
