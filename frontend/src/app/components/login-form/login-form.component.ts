import { Component, OnInit } from '@angular/core';
import { Http, URLSearchParams  } from '@angular/http';
import 'rxjs/add/operator/toPromise';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent implements OnInit {

  doLogin(){
    this.http.get('/idlogin').toPromise().then(res => {
      console.log(res);
    }, err => {
      console.log(err);
    });
    return false;
  }

  constructor(private http: Http) { }

  ngOnInit() {
  }

}
