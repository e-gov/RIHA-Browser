import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {SystemsService} from '../../services/systems.service';
import {classifiers, EnvironmentService} from '../../services/environment.service';
import {GeneralHelperService} from '../../services/general-helper.service';
import {ActivatedRoute} from '@angular/router';


@Component({
  selector: 'app-producer-search-filter',
  templateUrl: './producer-search-filter-component.html',
  styleUrls: ['./producer-search-filter-component.scss']
})
export class ProducerSearchFilterComponent implements OnInit {

  filters: {
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

  classifiers = classifiers;

  @Input()
  extendedSearch: boolean = false;

  @Input()
  showUserAndOrganizationFilters: boolean = false;

  @Input()
  searchFunction: any;

  @Output()
  performSearch = new EventEmitter();


  getOwnSystems() {
    this.performSearch.next(this.filters);
  }

  setTopicFilter(topic: string): void {
    this.filters.topic = topic;
  }


  getFilters(): any {
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

    return params;
  }


  hasActiveFilters(): boolean {
    for (let key in this.filters) {
      if (key != 'searchText' && this.filters[key]) {
        return true;
      }
    }
    return false;
  }

  clearFilters() {
    this.filters = {
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

  clearFiltersAndRefresh() {
    this.clearFilters();
  }


  constructor(private systemsService: SystemsService,
              private environmentService: EnvironmentService,
              private route: ActivatedRoute,
              public  generalHelperService: GeneralHelperService) {

  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.filters = {
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
    });
  }

}
