import {Component, Input, OnInit} from '@angular/core';
import {System} from '../../../models/system';
import {globals} from "../../../services/environment.service";
import {Router} from '@angular/router';
import {GeneralHelperService} from '../../../services/general-helper.service';

@Component({
  selector: 'app-producer-details-general',
  templateUrl: './producer-details-general.component.html',
  styleUrls: ['./producer-details-general.component.scss']
})
export class ProducerDetailsGeneralComponent implements OnInit {

  @Input() system: System;
  @Input() allowEdit: boolean;
  globals = globals;

  listSystemsWithTopic(topic){
    this.router.navigate(['/Infosüsteemid'], {queryParams: {topic: topic}});
  }

  constructor(private router: Router,  private helper: GeneralHelperService) { }

  ngOnInit() {
  }

}
