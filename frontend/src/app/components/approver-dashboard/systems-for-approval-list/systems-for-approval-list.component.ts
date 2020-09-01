import {Component, KeyValueDiffers, OnInit} from '@angular/core';
import {SystemsService} from '../../../services/systems.service';
import {ToastrService} from 'ngx-toastr';
import {GridData} from '../../../models/grid-data';
import {classifiers, EnvironmentService} from '../../../services/environment.service';
import {GeneralHelperService} from '../../../services/general-helper.service';
import * as moment from 'moment';
import {UserMatrix} from '../../../models/user-matrix';

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
    const momentDate = moment(date);
    return moment().diff(momentDate, 'days') > 0;
  }

  private getOpenApprovalRequestsWithoutDecisions(){
    this.systemsService.getOpenApprovalRequests(this.gridData.sort).subscribe( res => {
      this.approvalReqestsForDisplay = [];
      this.loaded = true;
      this.gridData.updateData(res);
      this.gridData.content.forEach(ar => {
        if (ar.events == null){
          this.approvalReqestsForDisplay.push(ar);
        } else if (ar.events){
          let hasDecision = false;
          const eventsCount = ar.events.length;
          for (let i = 0; i < eventsCount; i++){
            const event = ar.events[i];
            const activeOrganization = this.environmentService.getActiveUser() ? this.environmentService.getActiveUser().getActiveOrganization() : null;
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
    this.differ = differs.find({}).create();
  }

  ngOnInit() {
    this.gridData.changeSortOrder('creation_date', 'ASC');
    this.getOpenApprovalRequestsWithoutDecisions();
  }

  ngDoCheck() {
    const changes = this.differ.diff(this.environmentService.globalEnvironment);
    if (changes && (this.loaded || !this.environmentService.getUserMatrix().isOrganizationSelected)){
      this.loaded = false;
      this.getOpenApprovalRequestsWithoutDecisions();
    }
  }
}
