import { Component, OnInit, Input } from '@angular/core';
import { System } from '../../models/system';
import { SystemsService } from '../../services/systems.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-producer-edit-general',
  templateUrl: './producer-edit-general.component.html',
  styleUrls: ['./producer-edit-general.component.scss']
})
export class ProducerEditGeneralComponent implements OnInit {

  @Input() system: System;

  onSubmit(f) :void {
    if (f.valid) {
      this.systemsService.updateSystem(this.system).then(response => {
        this.router.navigate(['/Kirjelda/Vaata/', response.json().id]);
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
              private router: Router) { }

  ngOnInit() {
  }

}
