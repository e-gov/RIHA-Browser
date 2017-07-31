import { Component, OnInit } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { GridData } from '../../models/grid-data';
import { isNumber } from "util";

@Component({
  selector: 'app-browser-list',
  templateUrl: './browser-list.component.html',
  styleUrls: ['./browser-list.component.scss']
})
export class BrowserListComponent implements OnInit {

  gridData: GridData;
  filters: {
    owner: string,
    name: string
  };

  onPageChange(newPage){
    this.gridData.page = newPage - 1;
    this.getSystems();
  }

  onSortChange(property): void{
    this.gridData.changeSortOrder(property);
    this.getSystems();
  }

  getSystemStatus(system){
    if (system.details.meta && system.details.meta.system_status) {
      return system.details.meta.system_status.status;
    } else {
      return '';
    }
  }

  getApprovalStatus(system){
    if (system.details.meta && system.details.meta.approval_status) {
      return system.details.meta.approval_status.status;
    } else {
      return '';
    }
  }

  getSystems(): void {
    this.systemsService.getSystems(this.filters, this.gridData).then(
      res => {
        this.gridData.updateData(res.json());
    })
  }

  constructor(private systemsService: SystemsService) {
    this.gridData = new GridData();
    this.filters = {
      owner: null,
      name: null
    }
  }

  ngOnInit() {
    this.getSystems();
  }

}
