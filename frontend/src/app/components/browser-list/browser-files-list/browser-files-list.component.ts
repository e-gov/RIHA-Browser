import { Component, OnInit } from '@angular/core';
import { GeneralHelperService } from '../../../services/general-helper.service';
import { Location } from '@angular/common';
import { GridData } from '../../../models/grid-data';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-browser-files-list',
  templateUrl: './browser-files-list.component.html',
  styleUrls: ['./browser-files-list.component.scss']
})
export class BrowserFilesListComponent implements OnInit {

  public gridData: GridData = new GridData();
  public loaded: boolean = true;
  public filters: any = {
    searchText: null
  };

  public onPageChange(newPage){
    this.gridData.page = newPage - 1;
    this.getDataObjectFiles(this.gridData.page);
  }

  public onSortChange(property): void{
    this.gridData.changeSortOrder(property);
    this.getDataObjectFiles();
  }

  public getDataObjectFiles(page?){
    let params = this.helper.cloneObject(this.filters);

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

    let q = this.helper.generateQueryString(params);
    this.location.replaceState('/Andmeobjektid', q);

    this.loaded = true;
  }

  constructor(public helper: GeneralHelperService,
              private route: ActivatedRoute,
              private location: Location) { }

  ngOnInit() {
    this.route.queryParams.subscribe( params => {
      this.filters = {
        searchText: params['searchText']
      };

      this.gridData.changeSortOrder(params['sort'] || 'fileResourceName', params['dir'] || 'ASC');
      this.gridData.setPageFromUrl(params['page']);
    });
    if (this.filters.searchText){
      this.getDataObjectFiles();
    }
    this.helper.setRihaPageTitle('Andmeobjektid');
  }

}
