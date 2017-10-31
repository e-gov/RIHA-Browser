import { Component, OnInit } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { GridData } from '../../models/grid-data';
import { GeneralHelperService } from '../../services/general-helper.service';

@Component({
  selector: 'app-browser-list',
  templateUrl: './browser-list.component.html',
  styleUrls: ['./browser-list.component.scss']
})
export class BrowserListComponent implements OnInit {

  gridData: GridData = new GridData();
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

  getSystems(): void {
    this.systemsService.getSystems(this.filters, this.gridData).then(
      res => {
        this.gridData.updateData(res.json());
    })
  }

  constructor(private systemsService: SystemsService,
              public generalHelperService: GeneralHelperService) {
    this.filters = {
      ownerName: null,
      name: null
    }
  }

  ngOnInit() {
    this.gridData.changeSortOrder('meta.update_timestamp', 'DESC');
    this.getSystems();
  }

}
