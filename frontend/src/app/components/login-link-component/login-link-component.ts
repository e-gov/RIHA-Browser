import {Component, Input} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login-link',
  templateUrl: './login-link-component.html'
})
export class LoginLinkComponent  {

  @Input()
  loginBoxText: string;

  @Input()
  loginLinkText: string = 'Logi sisse';

  constructor(
    private router: Router) {

  }

  getCurrentUrl() {
    return this.router.url;
  }


}
