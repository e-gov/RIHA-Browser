import { Component, OnInit } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { GridData } from '../../models/grid-data';
import { GeneralHelperService } from '../../services/general-helper.service';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
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
    lastPositiveApprovalRequestType: string,
    dateCreatedFrom: string,
    dateCreatedTo: string,
    dateUpdatedFrom: string,
    dateUpdatedTo: string
  };
  extendedSearch: boolean = true;
  loaded: boolean = false;

  globals: any = G;

  onPageChange(newPage){
    this.gridData.page = newPage - 1;
    this.getSystems(this.gridData.page);
  }

  onSortChange(property): void{
    this.gridData.changeSortOrder(property);
    this.getSystems();
  }

  getSystems(page?): void {
    let params = this.generalHelperService.cloneObject(this.filters);
    if (params.dateCreatedFrom) {
      params.dateCreatedFrom = this.systemsService.dateObjToTimestamp(params.dateCreatedFrom, true);
    }
    if (params.dateCreatedTo) {
      params.dateCreatedTo = this.systemsService.dateObjToTimestamp(params.dateCreatedTo, true);
    }
    if (params.dateUpdatedFrom) {
      params.dateUpdatedFrom = this.systemsService.dateObjToTimestamp(params.dateUpdatedFrom, true);
    }
    if (params.dateUpdatedTo) {
      params.dateUpdatedTo = this.systemsService.dateObjToTimestamp(params.dateUpdatedTo, true);
    }

    let sortProperty = this.gridData.getSortProperty();
    if (sortProperty) {
      params.sort = sortProperty;
    }
    let sortOrder = this.gridData.getSortOrder();
    if (sortOrder) {
      params.dir = sortOrder;
    }
    if (page && page != 0) {
      params.page = page + 1;
    }

    let q = this.generalHelperService.generateQueryString(params);
    this.location.replaceState('/Infosüsteemid', q);
    this.gridData.page = page || 0;
    this.systemsService.getSystems(params, this.gridData).then(
      res => {
        this.gridData.updateData(res.json());
        if (this.gridData.getPageNumber() > 1 && this.gridData.getPageNumber() > this.gridData.totalPages) {
          this.getSystems();
        } else {
          this.loaded = true;
        }
        this.loaded = true;
      }, err => {
        this.loaded = true;
        this.toastrService.error('Serveri viga!');
      });
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
      lastPositiveApprovalRequestType: '',
      dateCreatedFrom: '',
      dateCreatedTo: '',
      dateUpdatedFrom: '',
      dateUpdatedTo: ''
    };
  }

  clearFiltersAndRefresh(){
    this.clearFilters();
    this.getSystems();
  }

  searchSystemsByTopic(topic){
    this.clearFilters();
    this.filters.topic = topic;
    this.extendedSearch = true;
    this.getSystems();
  }

  constructor(private systemsService: SystemsService,
              private route: ActivatedRoute,
              private location: Location,
              private toastrService: ToastrService,
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
        lastPositiveApprovalRequestType: params['lastPositiveApprovalRequestType'] || '',
        dateCreatedFrom: this.systemsService.timestampToDateObj(params['dateCreatedFrom']),
        dateCreatedTo: this.systemsService.timestampToDateObj(params['dateCreatedTo']),
        dateUpdatedFrom: this.systemsService.timestampToDateObj(params['dateUpdatedFrom']),
        dateUpdatedTo: this.systemsService.timestampToDateObj(params['dateUpdatedTo'])
      };

      this.gridData.changeSortOrder(params['sort'] || 'meta.update_timestamp', params['dir'] || 'DESC');
      this.gridData.setPageFromUrl(params['page']);
    });

    if (this.hasActiveFilters()){
      this.extendedSearch = true;
    }


    this.getSystems(this.gridData.page);
  }

}
