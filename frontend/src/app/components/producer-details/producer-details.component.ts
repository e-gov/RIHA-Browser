import { Component, OnInit } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { ActivatedRoute } from '@angular/router';
import { System } from '../../models/system';
import { WindowRefService } from '../../services/window-ref.service';

@Component({
  selector: 'app-producer-details',
  templateUrl: './producer-details.component.html',
  styleUrls: ['./producer-details.component.scss']
})
export class ProducerDetailsComponent implements OnInit {
  private system: System;
  private loaded: boolean = false;

  adjustSection(attempt){
    if (attempt < 5){
      let hash = this.winRef.nativeWindow.location.hash;
      if (hash){
        let elId = decodeURI(hash.replace('#',''));
        let el = this.winRef.nativeWindow.document.getElementById(elId);
        if (el){
          this.winRef.nativeWindow.scrollTo(el.offsetLeft,el.offsetTop);
        } else {
          attempt++;
          setTimeout(()=>{this.adjustSection(attempt)}, 1000);
        }
      }
    }
  }

  getSystem(id){
    this.systemsService.getSystem(id).then(response => {
      this.system = response.json();
      this.loaded = true;
      this.adjustSection(0);
    })
  }

  constructor(private systemsService: SystemsService,
              private route: ActivatedRoute,
              private winRef: WindowRefService) {
    this.system = new System();
  }

  ngOnInit() {
    this.loaded = false;
    this.route.params.subscribe( params => {
      this.getSystem(params['id']);
    });
  }

}
