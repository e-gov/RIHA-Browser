import { Component, OnInit } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-producer-edit',
  templateUrl: './producer-edit.component.html',
  styleUrls: ['./producer-edit.component.scss']
})
export class ProducerEditComponent implements OnInit {
  private system: any;
  private loaded: boolean = false;

  getSystem(id){
    this.systemsService.getSystem(id).then(response => {
      this.system = response.json();
      this.loaded = true;
    })
  }

  constructor(private systemsService: SystemsService,
              private route: ActivatedRoute) {
    this.system = {};

  }

  ngOnInit() {
    this.loaded = false;
    this.route.params.subscribe( params => {
      this.getSystem(params['id']);
    });
  }

}
