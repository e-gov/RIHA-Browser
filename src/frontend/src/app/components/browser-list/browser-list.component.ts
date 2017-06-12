import { Component, OnInit } from '@angular/core';
import { SystemsService } from '../../services/systems.service';

@Component({
  selector: 'app-browser-list',
  templateUrl: './browser-list.component.html',
  styleUrls: ['./browser-list.component.scss']
})
export class BrowserListComponent implements OnInit {

  systems: any[];

  getSystems(): void {
    this.systemsService.getSystems().then(
      res => {
        this.systems = res.json().content;
    })
  }

  constructor(private systemsService: SystemsService) {
    this.systems = [];
  }

  ngOnInit() {
    this.getSystems();
  }

}
