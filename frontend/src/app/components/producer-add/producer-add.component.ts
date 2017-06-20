import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-producer-add',
  templateUrl: './producer-add.component.html',
  styleUrls: ['./producer-add.component.scss']
})
export class ProducerAddComponent implements OnInit {

  system: any;

  constructor() {
    this.system = {};
  }

  ngOnInit() {
  }

}
