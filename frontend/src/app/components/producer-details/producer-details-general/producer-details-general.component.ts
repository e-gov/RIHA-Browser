import { Component, OnInit, Input } from '@angular/core';
import { System } from '../../../models/system';
import { G } from '../../../globals/globals';

@Component({
  selector: 'app-producer-details-general',
  templateUrl: './producer-details-general.component.html',
  styleUrls: ['./producer-details-general.component.scss']
})
export class ProducerDetailsGeneralComponent implements OnInit {

  @Input() system: System;
  @Input() allowEdit: boolean;
  globals: any = G;

  constructor() { }

  ngOnInit() {
  }

}
