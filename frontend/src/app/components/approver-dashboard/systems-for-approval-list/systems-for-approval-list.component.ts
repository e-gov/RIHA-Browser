import { Component, OnInit } from '@angular/core';
import { SystemsService } from '../../../services/systems.service';
import { ToastrService } from 'ngx-toastr';
import { GridData } from '../../../models/grid-data';

@Component({
  selector: 'app-systems-for-approval-list',
  templateUrl: './systems-for-approval-list.component.html',
  styleUrls: ['./systems-for-approval-list.component.scss']
})
export class SystemsForApprovalListComponent implements OnInit {
  public loaded: boolean = false;
  public gridData: GridData = new GridData();
  public approvalReqestsForDisplay = [];

  public onSortChange(property): void{
    this.gridData.changeSortOrder(property);
    this.getOpenApprovalRequestsWithoutDecisions();
  }

  private getOpenApprovalRequestsWithoutDecisions(){
    this.systemsService.getOpenApprovalRequests(this.gridData.sort).then( res => {
      this.loaded = true;
      this.gridData.updateData(res.json());
      this.approvalReqestsForDisplay = this.gridData.content;
    }, err => {
      this.loaded = true;
      this.toastrService.error('Serveri viga!');
    });
  }

  constructor(private systemsService: SystemsService,
              private toastrService: ToastrService) { }

  ngOnInit() {
    this.gridData.changeSortOrder('creation_date', 'ASC');
    this.getOpenApprovalRequestsWithoutDecisions();
  }

}
