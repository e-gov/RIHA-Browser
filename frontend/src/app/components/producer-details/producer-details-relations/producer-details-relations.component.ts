import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-producer-details-relations',
  templateUrl: './producer-details-relations.component.html',
  styleUrls: ['./producer-details-relations.component.scss']
})
export class ProducerDetailsRelationsComponent implements OnInit {

  @Input() allowEdit;

  seosed: any[] = [];

  openRelationsEdit(){};

  constructor() { }

  ngOnInit() {
  }

}
