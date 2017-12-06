import { Component, OnInit } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { EnvironmentService } from '../../services/environment.service';
import { GridData } from '../../models/grid-data';
import { UserMatrix } from '../../models/user-matrix';
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
export class ProducerListComponent implements OnInit {

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
    dateCreatedFrom: string,
    dateCreatedTo: string,
    dateUpdatedFrom: string,
    dateUpdatedTo: string
  };
  userMatrix: UserMatrix;

  extendedSearch: boolean = false;

  globals: any = G;

  onPageChange(newPage): void{
    this.gridData.page = newPage - 1;
    this.getOwnSystems();
  }

  onSortChange(property): void{
    this.gridData.changeSortOrder(property);
    this.getOwnSystems();
  }

  getOwnSystems(): void {
    if (this.userMatrix.isLoggedIn && this.userMatrix.isOrganizationSelected){
      let f = this.generalHelperService.cloneObject(this.filters);
      delete f.ownerName;
      delete f.ownerCode;
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
      this.location.replaceState('/Kirjelda', q);
      this.gridData.page = 0;
      this.systemsService.getOwnSystems(this.filters, this.gridData).then(
      res => {
        this.gridData.updateData(res.json());
      })
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
              public  generalHelperService: GeneralHelperService,
              private modalService: ModalHelperService) {
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
    this.getOwnSystems();
  }

}
