import { Component, OnInit } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { GridData } from '../../models/grid-data';
import { G } from '../../globals/globals';

@Component({
  selector: 'app-browser-list',
  templateUrl: './browser-list.component.html',
  styleUrls: ['./browser-list.component.scss']
})
export class BrowserListComponent implements OnInit {

  gridData: GridData;
  filters: {
    ownerName: string,
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

  getSystemStatusText(system){
    let statusDescription = 'määramata';
    if (system.details.meta && system.details.meta.system_status) {
      let status = system.details.meta.system_status.status;
      switch (status) {
        case G.system_status.IN_USE: {
          statusDescription = 'kasutusel';
          break;
        }
        case G.system_status.ESTABLISHING: {
          statusDescription = 'asutamisel';
          break;
        }
        case G.system_status.FINISHED: {
          statusDescription = 'lõpetatud';
          break
        }
      }
    }
    return statusDescription;
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
      ownerName: null,
      name: null
    }
  }

  ngOnInit() {
    this.getSystems();
  }

}
