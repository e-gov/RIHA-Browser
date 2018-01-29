import { Component, OnInit } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { ActivatedRoute } from '@angular/router';
import { System } from '../../models/system';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GeneralHelperService } from '../../services/general-helper.service';

@Component({
  selector: 'app-producer-edit',
  templateUrl: './producer-edit.component.html',
  styleUrls: ['./producer-edit.component.scss']
})
export class ProducerEditComponent implements OnInit {
  private system: System;
  public loaded: boolean;
  public notFound: boolean;

  getSystem(reference){
    this.systemsService.getSystem(reference).then(response => {
      let s = new System(response.json());
      this.generalHelperService.setRihaPageTitle(s.details.name);
      this.system = new System(this.systemsService.prepareSystemForDisplay(s));
      this.loaded = true;
    }, err => {
      let status = err.status;
      if (status == '404'){
        this.notFound = true;
        this.generalHelperService.setRihaPageTitle('Lehekülge ei leitud');
      } else if (status == '500'){
        this.toastrService.error('Serveri viga');
        this.router.navigate(['/']);
      } else {
        this.router.navigate(['/']);
      }
    });
  }

  constructor(private systemsService: SystemsService,
              private router: Router,
              private toastrService: ToastrService,
              private generalHelperService: GeneralHelperService,
              private route: ActivatedRoute) {
    this.system = new System();
  }

  ngOnInit() {
    this.loaded = false;
    this.notFound = false;
    this.system = new System();
    this.route.params.subscribe( params => {
      this.getSystem(params['reference']);
    });

    this.generalHelperService.setRihaPageTitle();
  }

}
