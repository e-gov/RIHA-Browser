import {AfterViewInit, Component, DoCheck, KeyValueDiffers, OnInit, ViewChild} from '@angular/core';
import {SystemsService} from '../../services/systems.service';
import {EnvironmentService} from '../../services/environment.service';
import {GridData} from '../../models/grid-data';
import {UserMatrix} from '../../models/user-matrix';
import {ToastrService} from 'ngx-toastr';
import {ModalHelperService} from '../../services/modal-helper.service';
import {ActiveOrganizationChooserComponent} from '../active-organization-chooser/active-organization-chooser.component';
import {GeneralHelperService} from '../../services/general-helper.service';
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';
import {System} from '../../models/system';
import _ from 'lodash';
import {ProducerSearchFilterComponent} from '../producer-search-filter/producer-search-filter-component';

@Component({
  selector: 'app-producer-list',
  templateUrl: './producer-list.component.html',
  styleUrls: ['./producer-list.component.scss']
})
export class ProducerListComponent implements OnInit, AfterViewInit, DoCheck {

  gridData: GridData  = new GridData();

  userMatrix: UserMatrix;
  loaded: boolean = false;
  differ: any;

  searchText: string;
  extendedSearch: boolean = false;


  @ViewChild(ProducerSearchFilterComponent)
  filterPanel: ProducerSearchFilterComponent;

  onPageChange(newPage): void{
    this.gridData.page = newPage - 1;
    this.getOwnSystems(this.gridData.page);
  }

  onSortChange(property): void{
    this.gridData.changeSortOrder(property);
    this.getOwnSystems();
  }

  _loadSystems(filters, page?): void {

    if (!(this.userMatrix.isLoggedIn && this.userMatrix.isOrganizationSelected)) {
      return;
    }

    let params = filters;
    params.searchText = this.searchText;
    delete params.ownerName;
    delete params.ownerCode;
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
    } else {
      this.gridData.page = 0;
    }

    let q = this.generalHelperService.generateQueryString(params);
    this.location.replaceState('/Kirjelda', q);

    this.systemsService.getOwnSystems(params, this.gridData).then(
      res => {
        this.gridData.updateData(res.json(), (content) => _.map(content, (contentElement) => new System(contentElement)));
        if (this.gridData.getPageNumber() > 1 && this.gridData.getPageNumber() > this.gridData.totalPages) {
          this.getOwnSystems();
        } else {
          this.loaded = true;
        }
      }, err => {
        this.loaded = true;
        this.toastrService.error('Serveri viga!');
      });
  }

  searchSystems(filters): void {
    this._loadSystems(filters);
  }


  getOwnSystems(page?): void {
    this._loadSystems(this.filterPanel.getFilters(), page);
  }

  openOrganizationsModal() {
    const modalRef = this.modalService.open(ActiveOrganizationChooserComponent);
    return false;
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
    this.getOwnSystems();
  }

  searchSystemsByTopic(topic){
    this.clearFilters();
    this.filterPanel.setTopicFilter(topic);

    this.extendedSearch = true;
    this.getOwnSystems();
  }

  constructor(private systemsService: SystemsService,
              private environmentService: EnvironmentService,
              private route: ActivatedRoute,
              private location: Location,
              private toastrService: ToastrService,
              private differs: KeyValueDiffers,
              public  generalHelperService: GeneralHelperService,
              private modalService: ModalHelperService) {
    this.differ = differs.find({}).create(null);
    this.userMatrix = this.environmentService.getUserMatrix();
  }

  ngOnInit() {
    this.route.queryParams.subscribe( params => {

      this.gridData.changeSortOrder(params['sort'] || 'meta.update_timestamp', params['dir'] || 'DESC');
      this.gridData.setPageFromUrl(params['page']);
    });

    this.generalHelperService.setRihaPageTitle('Minu infosüsteemid');
  }

  ngAfterViewInit() {

    if (this.hasActiveFilters()){
      this.extendedSearch = true;
    }

    this.getOwnSystems(this.gridData.page);
  }

  ngDoCheck() {
    var changes = this.differ.diff(this.environmentService.globalEnvironment);
    if (changes && (this.loaded || !this.userMatrix.isOrganizationSelected)){
      this.userMatrix = this.environmentService.getUserMatrix();
      this.getOwnSystems();
    }
  }

}
