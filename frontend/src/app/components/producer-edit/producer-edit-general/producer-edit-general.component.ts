import { Component, OnInit, Input } from '@angular/core';
import { System } from '../../../models/system';
import { SystemsService } from '../../../services/systems.service';
import { EnvironmentService } from "../../../services/environment.service";
import { WindowRefService } from '../../../services/window-ref.service';
import { Router, ActivatedRoute } from '@angular/router';
import { G } from '../../../globals/globals';

@Component({
  selector: 'app-producer-edit-general',
  templateUrl: './producer-edit-general.component.html',
  styleUrls: ['./producer-edit-general.component.scss']
})
export class ProducerEditGeneralComponent implements OnInit {

  @Input() system: System;
  shortName: string;
  alertConf: any = null;
  globals: any = G;
  timeoutId: any = null;

  onSubmit(f) :void {
    this.alertConf = null;
    if (f.valid) {
      this.systemsService.updateSystem(this.systemsService.prepareSystemForSending(this.system), this.shortName).then(response => {
        this.router.navigate(['/Kirjelda/Vaata/', response.json().details.short_name]);
      }, err => {
        this.system = this.systemsService.prepareSystemForDisplay(this.system);
        //show error on server     this.winRef.nativeWindow.scrollTo(0,0);
        this.winRef.nativeWindow.scrollTo(0,0);
        this.alertConf = {
          type: 'danger',
          heading: 'Viga',
          text: this.systemsService.getAlertText(err.json())
        };
        clearTimeout(this.timeoutId);
        this.timeoutId = setTimeout(()=>{
          this.alertConf = null
        }, 10000);
      });
    }
  }

  changeSystemStatus(status) {
    this.system.setStatus(status);
    if (status != G.system_status.IN_USE){
      this.system.details.meta.system_status.timestamp = null;
    }
    return false;
  }

  changeXRoadStatus(xRoadStatus) {
    this.system.setXRoadStatus(xRoadStatus);
    if (xRoadStatus != G.x_road_status.JOINED){
      this.system.details.meta.x_road_status.timestamp = null;
    }
    return false;
  }

  changeInDevelopmentStatus(inDevelopment){
    this.system.setInDevelopment(inDevelopment);
    return false;
  }

  canDescribe(){
    let user = this.environmentService.getActiveUser();
    let ret = false;
    if (user){
      ret = user.canEdit(this.system.getOwnerCode());
    }

    return ret;
  }

  isLoggedIn(){
    return this.environmentService.getActiveUser() != null;
  }

  constructor(private systemsService: SystemsService,
              private router: Router,
              private route: ActivatedRoute,
              private winRef: WindowRefService,
              private environmentService: EnvironmentService) {
  }

  ngOnInit() {
    this.route.params.subscribe( params => {
      this.shortName = params['short_name'];
    });
  }

}
