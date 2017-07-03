import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-producer-edit-objects',
  templateUrl: './producer-edit-objects.component.html',
  styleUrls: ['./producer-edit-objects.component.scss']
})
export class ProducerEditObjectsComponent implements OnInit {

  objects: any[];

  addObject(): void {

  }

  constructor() {
    this.objects = [];
  }

  ngOnInit() {
  }

}
