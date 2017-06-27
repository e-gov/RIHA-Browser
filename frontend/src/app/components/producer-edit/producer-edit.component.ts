import { Component, OnInit } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { ActivatedRoute, Router } from '@angular/router';
import { System } from '../../models/system';

@Component({
  selector: 'app-producer-edit',
  templateUrl: './producer-edit.component.html',
  styleUrls: ['./producer-edit.component.scss']
})
export class ProducerEditComponent implements OnInit {
  private system: System;
  private loaded: boolean = false;

  getSystem(id){
    this.systemsService.getSystem(id).then(response => {
      this.system.setData(response.json());
      this.loaded = true;
    })
  }

  onSubmit(f) :void {
    if (f.valid) {
      this.systemsService.updateSystem(this.system).then(response => {
        this.router.navigate(['/Kirjelda/Vaata/', this.system.id]);
      });
    }
  }

  changeSystemStatus(statusCode) {
    this.system.setStatus(statusCode);
    return false;
  }

  changeInDevelopmentStatus(inDevelopment){
    this.system.setInDevelopment(inDevelopment);
    return false;
  }

  constructor(private systemsService: SystemsService,
              private route: ActivatedRoute,
              private router: Router) {
    this.system = new System();

  }

  ngOnInit() {
    this.loaded = false;
    this.route.params.subscribe( params => {
      this.getSystem(params['id']);
    });
  }

}
