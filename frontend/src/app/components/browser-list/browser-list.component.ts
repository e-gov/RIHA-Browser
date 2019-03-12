import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {SystemsService} from '../../services/systems.service';
import {GridData} from '../../models/grid-data';
import {GeneralHelperService} from '../../services/general-helper.service';
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';
import {ToastrService} from 'ngx-toastr';
import {classifiers} from "../../services/environment.service";
import {System} from '../../models/system';
import _ from 'lodash';
import {ProducerSearchFilterComponent} from '../producer-search-filter/producer-search-filter-component';

@Component({
  selector: 'app-browser-list',
  templateUrl: './browser-list.component.html',
  styleUrls: ['./browser-list.component.scss']
})
export class BrowserListComponent implements OnInit, AfterViewInit {

  gridData: GridData = new GridData();

  @ViewChild(ProducerSearchFilterComponent)
  filterPanel: ProducerSearchFilterComponent;

  searchText: string;

  extendedSearch: boolean = false;
  loaded: boolean = false;

  classifiers = classifiers;

  onPageChange(newPage){
    this.gridData.page = newPage - 1;
    this.getSystems(this.gridData.page);
  }

  onSortChange(property): void{
    this.gridData.changeSortOrder(property);
    this.getSystems();
  }

  _loadSystems(filters, page?) {
    let params = this.generalHelperService.cloneObject(filters);
    params.searchText = this.searchText;

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

        this.gridData.updateData(res.json(), (content) => _.map(content, (contentElement) => new System(contentElement)));
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

  searchSystems(filters): void {
    this._loadSystems(filters, 0);
  }

  getSystems(page?): void {
    this._loadSystems(this.filterPanel.getFilters(), page);
  }

  toggleSearchPanel(){
    this.extendedSearch = !this.extendedSearch;
    return false;
  }

  hasActiveFilters(): boolean{
    return this.filterPanel.hasActiveFilters();
  }

  clearFilters(){
    this.filterPanel.clearFilters();

  }

  clearFiltersAndRefresh(){
    this.clearFilters();
    this.getSystems();
  }

  searchSystemsByTopic(topic){
    this.clearFilters();
    this.filterPanel.setTopicFilter(topic);
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
      this.searchText = params['searchText'];
      this.gridData.changeSortOrder(params['sort'] || 'meta.update_timestamp', params['dir'] || 'DESC');
      this.gridData.setPageFromUrl(params['page']);
    });

    this.generalHelperService.setRihaPageTitle('Infosüsteemid');
  }


  ngAfterViewInit() {

    if (this.hasActiveFilters()){
      this.extendedSearch = true;
    }

    this.getSystems(this.gridData.page);
  }

}
