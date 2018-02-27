import { Component, OnInit, DoCheck, KeyValueDiffers } from '@angular/core';
import { SystemsService } from '../../../services/systems.service';
import { ToastrService } from 'ngx-toastr';
import { GridData } from '../../../models/grid-data';
import { EnvironmentService } from '../../../services/environment.service';
import { GeneralHelperService } from '../../../services/general-helper.service';
import { G } from '../../../globals/globals';
import * as moment from 'moment';
import { UserMatrix } from '../../../models/user-matrix';

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
  private differ: any;
  private userMatrix: UserMatrix;

  public onSortChange(property): void{
    this.gridData.changeSortOrder(property);
    this.getOpenApprovalRequestsWithoutDecisions();
  }

  public isOverdue(date){
    let momentDate = moment(date);
    return moment().diff(momentDate, 'days') > 30;
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
      this.helper.adjustSection();
    }, err => {
      this.loaded = true;
      this.toastrService.error('Serveri viga!');
    });
  }

  constructor(private systemsService: SystemsService,
              private helper: GeneralHelperService,
              private environmentService: EnvironmentService,
              private differs: KeyValueDiffers,
              private toastrService: ToastrService) {
    this.differ = differs.find({}).create(null);
  }

  ngOnInit() {
    this.gridData.changeSortOrder('creation_date', 'ASC');
    this.getOpenApprovalRequestsWithoutDecisions();
  }

  ngDoCheck() {
    var changes = this.differ.diff(this.environmentService.globalEnvironment);
    if (changes && (this.loaded || !this.environmentService.getUserMatrix().isOrganizationSelected)){
      this.loaded = false;
      this.getOpenApprovalRequestsWithoutDecisions();
    }
  }
}
