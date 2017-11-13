import { Component, OnInit, Input } from '@angular/core';
import { System } from '../../../models/system';
import { G } from '../../../globals/globals';
import { Router } from '@angular/router';

@Component({
  selector: 'app-producer-details-general',
  templateUrl: './producer-details-general.component.html',
  styleUrls: ['./producer-details-general.component.scss']
})
export class ProducerDetailsGeneralComponent implements OnInit {

  @Input() system: System;
  @Input() allowEdit: boolean;
  globals: any = G;

  listSystemsWithTopic(topic){
    this.router.navigate(['/Infosüsteemid'], {queryParams: {topic: topic}});
  }

  constructor(private router: Router) { }

  ngOnInit() {
  }

}
