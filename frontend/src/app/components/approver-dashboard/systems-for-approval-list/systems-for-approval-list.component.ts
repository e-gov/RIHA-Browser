import { Component, OnInit, KeyValueDiffers } from '@angular/core';
import { SystemsService } from '../../../services/systems.service';
import { ToastrService } from 'ngx-toastr';
import { GridData } from '../../../models/grid-data';
import { EnvironmentService, classifiers } from '../../../services/environment.service';
import { GeneralHelperService } from '../../../services/general-helper.service';
import * as moment from 'moment';
import { UserMatrix } from '../../../models/user-matrix';

@Component({
  selector: 'app-systems-for-approval-list',
  templateUrl: './systems-for-approval-list.component.html',
  styleUrls: ['./systems-for-approval-list.component.scss']
})
export class SystemsForApprovalListComponent implements OnInit {
  classifiers = classifiers;
  public loaded: boolean = false;
  public gridData: GridData = new GridData();
  public approvalReqestsForDisplay = [];
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
            if (event.type == this.classifiers.event_type.DECISION.code && activeOrganization && activeOrganization.code == event.organizationCode){
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
