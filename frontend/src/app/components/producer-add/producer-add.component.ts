import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SystemsService } from '../../services/systems.service';
import { EnvironmentService} from "../../services/environment.service";
import { Location } from '@angular/common';
import { ToastrService } from "ngx-toastr";

@Component({
  selector: 'app-producer-add',
  templateUrl: './producer-add.component.html',
  styleUrls: ['./producer-add.component.scss']
})
export class ProducerAddComponent implements OnInit {

  onSubmit(f) :void {
    if (f.valid){
      this.systemsService.addSystem(f.value).then(
        res => {
          this.router.navigate(['/Kirjelda/Vaata', res.json().details.short_name]);
        }, err => {
          this.toastrService.error('Infosüsteemi lisamine ebaõnnestus');
          this.location.back();
        })
    }
  }

  isLoggedIn(){
    return this.environmentService.getActiveUser() != null;
  }

  canDescribe(){
    let ret = false;
    let user = this.environmentService.getActiveUser();
    if (user){
      ret = user.getRoles().indexOf('ROLE_KIRJELDAJA') != -1;
    }
    return ret;
  }

  constructor(private systemsService: SystemsService,
              private environmentService: EnvironmentService,
              private router: Router,
              private location: Location,
              private toastrService: ToastrService) {
  }

  ngOnInit() {
  }

}
