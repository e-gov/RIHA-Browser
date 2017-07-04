import { Component, OnInit } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { ActivatedRoute } from '@angular/router';
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

  constructor(private systemsService: SystemsService,
              private route: ActivatedRoute) {
    this.system = new System();

  }

  ngOnInit() {
    this.loaded = false;
    this.system = new System();
    this.route.params.subscribe( params => {
      this.getSystem(params['id']);
    });
  }

}
