import { Component, OnInit } from '@angular/core';
import { GeneralHelperService } from '../../../services/general-helper.service';
import { Location } from '@angular/common';
import { GridData } from '../../../models/grid-data';
import { ActivatedRoute } from '@angular/router';
import { SystemsService } from '../../../services/systems.service';

@Component({
  selector: 'app-browser-files-list',
  templateUrl: './browser-files-list.component.html',
  styleUrls: ['./browser-files-list.component.scss'],
  standalone: false,
})
export class BrowserFilesListComponent implements OnInit {
  public gridData: GridData = new GridData();
  public loaded: boolean = false;
  filters: {
    searchText: string;
    searchName: string;
    infosystem: string;
    dataObjectName: string;
    comment: string;
    parentObject: string;
    personalData: string;
  };

  extendedSearch: boolean = false;

  toggleSearchPanel() {
    this.extendedSearch = !this.extendedSearch;
    return false;
  }

  public onPageChange(newPage) {
    this.gridData.page = newPage - 1;
    this.getDataObjectFiles(this.gridData.page);
  }

  public onSortChange(property): void {
    this.gridData.changeSortOrder(property);
    this.getDataObjectFiles();
  }

  public getDataObjectFiles(page?) {
    // Validate that at least one search field has at least 2 characters
    if (!this.hasValidSearchInput()) {
      this.helper.showError('Palun sisesta vähemalt ühte otsingukriteeriumisse vähemalt 2 tähemärki');
      return;
    }

    const params = this.helper.cloneObject(this.filters);

    const sortProperty = this.gridData.getSortProperty();
    if (sortProperty) {
      params.sort = sortProperty;
    }
    const sortOrder = this.gridData.getSortOrder();
    if (sortOrder) {
      params.dir = sortOrder;
    }
    if (page && page != 0) {
      params.page = page + 1;
    }

    this.gridData.page = page || 0;

    const q = this.helper.generateQueryString(params);
    this.location.replaceState('/Andmeobjektid', q);

    this.systemsService.getSystemsDataObjects(this.filters, this.gridData).subscribe(
      res => {
        this.gridData.updateData(res);
        if (this.gridData.getPageNumber() > 1 && this.gridData.getPageNumber() > this.gridData.totalPages) {
          this.getDataObjectFiles();
        }
        this.loaded = true;
      },
      err => {
        this.loaded = true;
        this.helper.showError();
      },
    );
  }

  private hasValidSearchInput(): boolean {
    const searchFields = [
      this.filters.searchText,
      this.filters.searchName,
      this.filters.infosystem,
      this.filters.dataObjectName,
      this.filters.comment,
      this.filters.parentObject
    ];

    // Check if at least one field has 2 or more characters
    // personalData is excluded as it's a dropdown selection
    return searchFields.some(field => field && field.trim().length >= 2);
  }

  constructor(
    public helper: GeneralHelperService,
    private route: ActivatedRoute,
    private systemsService: SystemsService,
    private location: Location,
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.filters = {
        searchText: params['searchText'],
        searchName: params['searchName'],
        infosystem: params['infosystem'],
        dataObjectName: params['dataObjectName'],
        comment: params['comment'],
        parentObject: params['parentObject'],
        personalData: params['personalData'] || '',
      };

      this.gridData.changeSortOrder(params['sort'] || 'file_resource_name', params['dir'] || 'ASC');
      this.gridData.setPageFromUrl(params['page']);
    });
    if (this.route.queryParams['page']) {
      this.gridData.setPageFromUrl(this.route.queryParams['page']);
    }
    if (this.filtersNotEmpty()) {
      this.getDataObjectFiles(this.gridData.page);
    }
    this.helper.setRihaPageTitle('Andmeobjektid');
  }

  private filtersNotEmpty(): boolean {
    return this.hasValidSearchInput() || (this.filters.personalData && this.filters.personalData !== '');
  }
}
