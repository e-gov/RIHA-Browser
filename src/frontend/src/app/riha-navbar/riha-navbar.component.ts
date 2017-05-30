import { Component, OnInit } from '@angular/core';
import {JsonDataService} from '../json-data.service';
import {TranslateService, TranslatePipe} from '@ngx-translate/core';

@Component({
  selector: 'app-riha-navbar',
  templateUrl: './riha-navbar.component.html',
  styleUrls: ['./riha-navbar.component.scss']
})
export class RihaNavbarComponent implements OnInit {
  private routes = [];

  constructor(private jsonDataService: JsonDataService) {
    jsonDataService.routes.subscribe(this.updateRoutes.bind(this));
  }

  updateRoutes(routes) {
    this.routes = routes;
  }

  ngOnInit() {
  }

}
