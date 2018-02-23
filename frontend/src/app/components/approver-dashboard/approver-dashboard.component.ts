import { Component, OnInit } from '@angular/core';
import { GeneralHelperService } from '../../services/general-helper.service';
import { EnvironmentService } from '../../services/environment.service';

@Component({
  selector: 'app-approver-dashboard',
  templateUrl: './approver-dashboard.component.html',
  styleUrls: ['./approver-dashboard.component.scss']
})
export class ApproverDashboardComponent implements OnInit {

  constructor(public helper: GeneralHelperService,
              public generalHelperService: GeneralHelperService,
              public environmentService: EnvironmentService) { }

  ngOnInit() {
  }

}
