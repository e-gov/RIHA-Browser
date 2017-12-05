import { Component, OnInit } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { GridData } from '../../models/grid-data';
import { GeneralHelperService } from '../../services/general-helper.service';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { G } from '../../globals/globals';

@Component({
  selector: 'app-browser-list',
  templateUrl: './browser-list.component.html',
  styleUrls: ['./browser-list.component.scss']
})
export class BrowserListComponent implements OnInit {

  gridData: GridData = new GridData();
  filters: {
    searchText: string,
    ownerName: string,
    ownerCode: string,
    purpose: string,
    name: string,
    topic: string,
    systemStatus: string,
    xRoadStatus: string,
    developmentStatus: string,
    dateCreatedFrom: string,
    dateCreatedTo: string,
    dateUpdatedFrom: string,
    dateUpdatedTo: string
  };
  extendedSearch: boolean = false;

  globals: any = G;

  onPageChange(newPage){
    this.gridData.page = newPage - 1;
    this.getSystems();
  }

  onSortChange(property): void{
    this.gridData.changeSortOrder(property);
    this.getSystems();
  }

  getSystems(): void {
    let f = this.generalHelperService.cloneObject(this.filters);
    if (f.dateCreatedFrom){
      f.dateCreatedFrom = this.systemsService.dateObjToTimestamp(f.dateCreatedFrom, true);
    }
    if (f.dateCreatedTo){
      f.dateCreatedTo = this.systemsService.dateObjToTimestamp(f.dateCreatedTo, true);
    }
    if (f.dateUpdatedFrom){
      f.dateUpdatedFrom = this.systemsService.dateObjToTimestamp(f.dateUpdatedFrom, true);
    }
    if (f.dateUpdatedTo){
      f.dateUpdatedTo = this.systemsService.dateObjToTimestamp(f.dateUpdatedTo, true);
    }
    let q = this.generalHelperService.generateQueryString(f);
    this.location.replaceState('/Infosüsteemid', q);
    this.systemsService.getSystems(f, this.gridData).then(
      res => {
        this.gridData.updateData(res.json());
    })
  }

  toggleSearchPanel(){
    this.extendedSearch = !this.extendedSearch;
    return false;
  }

  hasActiveFilters(): boolean{
    for (let key in this.filters) {
      if (key != 'searchText' && this.filters[key]){
        return true;
      }
    }
    return false;
  }

  clearFilters(){
    this.filters = {
      searchText: '',
      ownerName: '',
      ownerCode: '',
      purpose: '',
      name: '',
      topic: '',
      systemStatus: '',
      xRoadStatus: '',
      developmentStatus: '',
      dateCreatedFrom: '',
      dateCreatedTo: '',
      dateUpdatedFrom: '',
      dateUpdatedTo: ''
    };
    this.getSystems();
  }

  constructor(private systemsService: SystemsService,
              private route: ActivatedRoute,
              private location: Location,
              public generalHelperService: GeneralHelperService) {
  }

  ngOnInit() {
    this.route.queryParams.subscribe( params => {
      this.filters = {
        searchText: params['searchText'],
        ownerName: params['ownerName'],
        ownerCode: params['ownerCode'],
        purpose: params['purpose'],
        name: params['name'],
        topic: params['topic'],
        systemStatus: params['systemStatus'] || '',
        xRoadStatus: params['xRoadStatus'] || '',
        developmentStatus: params['developmentStatus'] || '',
        dateCreatedFrom: this.systemsService.timestampToDateObj(params['dateCreatedFrom']),
        dateCreatedTo: this.systemsService.timestampToDateObj(params['dateCreatedTo']),
        dateUpdatedFrom: this.systemsService.timestampToDateObj(params['dateUpdatedFrom']),
        dateUpdatedTo: this.systemsService.timestampToDateObj(params['dateUpdatedTo'])
      };
    });

    if (this.hasActiveFilters()){
      this.extendedSearch = true;
    }

    this.gridData.changeSortOrder('meta.update_timestamp', 'DESC');
    this.getSystems();
  }

}
