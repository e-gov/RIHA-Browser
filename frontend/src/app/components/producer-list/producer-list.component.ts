import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-producer-list',
  templateUrl: './producer-list.component.html',
  styleUrls: ['./producer-list.component.scss']
})
export class ProducerListComponent implements OnInit {

  systems: any[];

  constructor() {
    this.systems = [];
  }

  ngOnInit() {
  }

}
