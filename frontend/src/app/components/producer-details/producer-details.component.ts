import { Component, OnInit } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { ActivatedRoute } from '@angular/router';
import { System } from '../../models/system';

@Component({
  selector: 'app-producer-details',
  templateUrl: './producer-details.component.html',
  styleUrls: ['./producer-details.component.scss']
})
export class ProducerDetailsComponent implements OnInit {
  private system: System;
  private loaded: boolean = false;

  getSystem(id){
    this.systemsService.getSystem(id).then(response => {
      this.system = response.json();
      this.loaded = true;
    })
  }

  constructor(private systemsService: SystemsService,
              private route: ActivatedRoute) {
    this.system = new System();

  }

  ngOnInit() {
    this.loaded = false;
    this.route.params.subscribe( params => {
      this.getSystem(params['id']);
    });
  }

}
