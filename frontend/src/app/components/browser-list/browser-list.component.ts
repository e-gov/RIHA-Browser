import { Component, OnInit } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { GridData } from '../../models/grid-data';
import { GeneralHelperService } from '../../services/general-helper.service';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

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
    let q = this.generalHelperService.generateQueryString(this.filters);
    this.location.replaceState('/Infosüsteemid', q);
    this.systemsService.getSystems(this.filters, this.gridData).then(
      res => {
        this.gridData.updateData(res.json());
    })
  }

  constructor(private systemsService: SystemsService,
              private route: ActivatedRoute,
              private location: Location,
              public generalHelperService: GeneralHelperService) {
  }

  ngOnInit() {
    this.route.queryParams.subscribe( params => {
      this.filters = {
        ownerName: params['ownerName'],
        name: params['name']
      };
    });

    this.gridData.changeSortOrder('meta.update_timestamp', 'DESC');
    this.getSystems();
  }

}
