import {Component, Input, OnInit} from '@angular/core';
import {System} from '../../../models/system';
import {SystemsService} from '../../../services/systems.service';
import {classifiers, EnvironmentService} from "../../../services/environment.service";
import {WindowRefService} from '../../../services/window-ref.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-producer-edit-general',
  templateUrl: './producer-edit-general.component.html',
  styleUrls: ['./producer-edit-general.component.scss']
})
export class ProducerEditGeneralComponent implements OnInit {

  @Input() system: System;
  reference: string;
  alertConf: any = null;
  classifiers = classifiers;
  timeoutId: any = null;
  isChanged: boolean = false;
  standard: any = null;

  onSubmit(f) :void {
    this.alertConf = null;
    if (f.valid) {
      this.systemsService.updateSystem(this.systemsService.prepareSystemForSending(this.system), this.reference).subscribe(responseSystem => {
        this.router.navigate(['/Kirjelda/Vaata/', responseSystem.details.short_name]);
      }, err => {
        this.system = this.systemsService.prepareSystemForDisplay(this.system);
        //show error on server     this.winRef.nativeWindow.scrollTo(0,0);
        this.winRef.nativeWindow.scrollTo(0,0);
        this.alertConf = {
          type: 'danger',
          heading: 'Viga',
          text: this.systemsService.getAlertText(err)
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
    if (status != classifiers.system_status.IN_USE.code){
      this.system.details.meta.system_status.timestamp = null;
    }
    this.isChanged = true;
    return false;
  }

  changeStandardType() {
    if (this.system.getStandardInformationSystem() == false){
      this.system.setStandardInformationSystem(true);
    } else if (this.system.getStandardInformationSystem() == true){
      this.system.setStandardInformationSystem(false);
    }
  }

  changeXRoadStatus(xRoadStatus) {
    this.system.setXRoadStatus(xRoadStatus);
    if (xRoadStatus != classifiers.x_road_status.JOINED.code){
      this.system.details.meta.x_road_status.timestamp = null;
    }
    this.isChanged = true;
    return false;
  }

  changeInDevelopmentStatus(inDevelopment){
    this.system.setInDevelopment(inDevelopment);
    this.isChanged = true;
    return false;
  }

  canDescribe(){
    const user = this.environmentService.getActiveUser();
    let ret = false;
    if (user){
      ret = user.canEdit(this.system.getOwnerCode());
    }

    return ret;
  }

  isLoggedIn(){
    return typeof this.environmentService.getActiveUser() !== 'undefined' && this.environmentService.getActiveUser() != null;
  }

  goBackToDetails(f){
    if (f.form.dirty || this.isChanged){
      if (confirm('Oled väljades muudatusi teinud. Kui navigeerid siit ära ilma salvestamata, siis sinu muudatused kaovad.')){
        this.router.navigate(['/Kirjelda/Vaata', this.reference]);
      } else {
        return false;
      }
    } else {
      this.router.navigate(['/Kirjelda/Vaata', this.reference]);
    }
  }

  constructor(private systemsService: SystemsService,
              private router: Router,
              private route: ActivatedRoute,
              private winRef: WindowRefService,
              private environmentService: EnvironmentService) {
  }

  ngOnInit() {
    this.route.params.subscribe( params => {
      this.reference = params['reference'];
    });
  }
}
