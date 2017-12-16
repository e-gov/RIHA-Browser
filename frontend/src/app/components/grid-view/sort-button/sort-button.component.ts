import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { GridData } from '../../../models/grid-data';

@Component({
  selector: 'app-sort-button',
  templateUrl: './sort-button.component.html',
  styleUrls: ['./sort-button.component.scss']
})
export class SortButtonComponent implements OnInit {

  @Input() sortProperty: string;
  @Input() labelText: string;
  @Input() gridData: GridData;
  @Output() buttonClick = new EventEmitter<string>();

  getSortIcon(){
    let ret = 'fa-sort';
    let activeSortProperty = null;
    let asc = null;

    if (this.gridData.sort){
      asc = this.gridData.sort.charAt(0) == '-';
      activeSortProperty = asc ? this.gridData.sort.substring(1) : this.gridData.sort;
    }

    if (activeSortProperty == this.sortProperty){
      ret = asc ? 'fa-sort-asc' : 'fa-sort-desc';
    }

    return ret;
  }

  onButtonClick(){
    this.buttonClick.emit(this.sortProperty);
  }

  constructor() { }

  ngOnInit() {
  }

}
