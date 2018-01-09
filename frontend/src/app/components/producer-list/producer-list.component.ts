import { Component, OnInit, DoCheck, KeyValueDiffers } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { EnvironmentService } from '../../services/environment.service';
import { GridData } from '../../models/grid-data';
import { UserMatrix } from '../../models/user-matrix';
import { ToastrService } from 'ngx-toastr';
import { ModalHelperService } from '../../services/modal-helper.service';
import { ActiveOrganizationChooserComponent } from '../active-organization-chooser/active-organization-chooser.component';
import { GeneralHelperService } from '../../services/general-helper.service';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { G } from '../../globals/globals';

@Component({
  selector: 'app-producer-list',
  templateUrl: './producer-list.component.html',
  styleUrls: ['./producer-list.component.scss']
})
export class ProducerListComponent implements OnInit, DoCheck {

  gridData: GridData  = new GridData();
  filters: {
    searchText: string,
    purpose: string,
    name: string,
    shortName: string,
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
  userMatrix: UserMatrix;
  loaded: boolean = false;
  differ: any;

  extendedSearch: boolean = true;

  globals: any = G;

  onPageChange(newPage): void{
    this.gridData.page = newPage - 1;
    this.getOwnSystems(this.gridData.page);
  }

  onSortChange(property): void{
    this.gridData.changeSortOrder(property);
    this.getOwnSystems();
  }

  getOwnSystems(page?): void {
    if (this.userMatrix.isLoggedIn && this.userMatrix.isOrganizationSelected) {
      let params = this.generalHelperService.cloneObject(this.filters);
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
      }

      let q = this.generalHelperService.generateQueryString(params);
      this.location.replaceState('/Kirjelda', q);
      this.gridData.page = page || 0;
      this.systemsService.getOwnSystems(params, this.gridData).then(
        res => {
          this.gridData.updateData(res.json());
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
      purpose: '',
      name: '',
      shortName: '',
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
    this.getOwnSystems();
  }

  searchSystemsByTopic(topic){
    this.clearFilters();
    this.filters.topic = topic;
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
      this.filters = {
        searchText: params['searchText'],
        purpose: params['purpose'],
        name: params['name'],
        shortName: params['shortName'],
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
