import { Component, OnInit } from '@angular/core';
import { GeneralHelperService } from '../../../services/general-helper.service';
import { GridData } from '../../../models/grid-data';

@Component({
  selector: 'app-browser-files-list',
  templateUrl: './browser-files-list.component.html',
  styleUrls: ['./browser-files-list.component.scss']
})
export class BrowserFilesListComponent implements OnInit {

  public gridData: GridData = new GridData();
  public loaded: boolean = false;
  public filters: any = {
    searchText: null
  };

  public getDataObjectFiles(){
    this.loaded = true;
  }

  constructor(private helper: GeneralHelperService) { }

  ngOnInit() {
    this.helper.setRihaPageTitle('Andmeobjektid');
  }

}
