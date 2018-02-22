import { Component, OnInit } from '@angular/core';
import { SystemsService } from '../../../services/systems.service';
import { ToastrService } from 'ngx-toastr';
import { GridData } from '../../../models/grid-data';
import { EnvironmentService } from '../../../services/environment.service';
import { G } from '../../../globals/globals';

@Component({
  selector: 'app-systems-for-approval-list',
  templateUrl: './systems-for-approval-list.component.html',
  styleUrls: ['./systems-for-approval-list.component.scss']
})
export class SystemsForApprovalListComponent implements OnInit {
  public loaded: boolean = false;
  public gridData: GridData = new GridData();
  public approvalReqestsForDisplay = [];
  private globals: any = G;

  public onSortChange(property): void{
    this.gridData.changeSortOrder(property);
    this.getOpenApprovalRequestsWithoutDecisions();
  }

  private getOpenApprovalRequestsWithoutDecisions(){
    this.systemsService.getOpenApprovalRequests(this.gridData.sort).then( res => {
      this.approvalReqestsForDisplay = [];
      this.loaded = true;
      this.gridData.updateData(res.json());
      this.gridData.content.forEach(ar => {
        if (ar.events == null){
          this.approvalReqestsForDisplay.push(ar);
        } else if (ar.events){
          let hasDecision = false;
          let eventsCount = ar.events.length;
          for (let i = 0; i < eventsCount; i++){
            let event = ar.events[i];
            let activeOrganization = this.environmentService.getActiveUser().getActiveOrganization();
            if (event.type == this.globals.event_type.DECISION && activeOrganization && activeOrganization.code == event.organizationCode){
              hasDecision = true;
              break;
            }
          }
          if (!hasDecision){
            this.approvalReqestsForDisplay.push(ar);
          }
        }
      });
    }, err => {
      this.loaded = true;
      this.toastrService.error('Serveri viga!');
    });
  }

  constructor(private systemsService: SystemsService,
              private environmentService: EnvironmentService,
              private toastrService: ToastrService) { }

  ngOnInit() {
    this.gridData.changeSortOrder('creation_date', 'ASC');
    this.getOpenApprovalRequestsWithoutDecisions();
  }

}
