import { Component, OnInit, Input } from '@angular/core';
import { System } from '../../../models/system';
import { SystemsService } from '../../../services/systems.service';
import { WindowRefService } from '../../../services/window-ref.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-producer-edit-general',
  templateUrl: './producer-edit-general.component.html',
  styleUrls: ['./producer-edit-general.component.scss']
})
export class ProducerEditGeneralComponent implements OnInit {

  @Input() system: System;
  showAlert: boolean;
  alertConf: any;

  showValidationError() {
    this.alertConf = {
      type: 'danger',
      heading: 'Viga',
      text: 'Infosüsteemi valideerimise viga'
    };
    this.showAlert = true;
    this.winRef.nativeWindow.scrollTo(0,0);
    setTimeout(() => {this.showAlert = false}, 5000);
  }

  onSubmit(f) :void {
    if (f.valid) {
      this.systemsService.updateSystem(this.system).then(response => {
        this.router.navigate(['/Kirjelda/Vaata/', response.json().id]);
      }, error => {
        this.showValidationError();
      });
    }
  }

  changeSystemStatus(statusCode) {
    this.system.setStatus(statusCode);
    if (statusCode == 1){
      this.system.details.active_since = null;
    }
    return false;
  }

  changeInDevelopmentStatus(inDevelopment){
    this.system.setInDevelopment(inDevelopment);
    return false;
  }

  constructor(private systemsService: SystemsService,
              private router: Router,
              private winRef: WindowRefService) {
    this.showAlert = false;
    this.alertConf = {};
  }

  ngOnInit() {
  }

}
