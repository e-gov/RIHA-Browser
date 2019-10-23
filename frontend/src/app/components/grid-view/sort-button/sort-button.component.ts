import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {GridData} from '../../../models/grid-data';

@Component({
  selector: 'app-sort-button',
  templateUrl: './sort-button.component.html',
  styleUrls: ['./sort-button.component.scss']
})
export class SortButtonComponent implements OnInit {

  @Input() sortProperty: string;
  @Input() cssClass: string;
  @Input() labelText: string;
  @Input() gridData: GridData;
  @Output() buttonClick = new EventEmitter<string>();

  getSortIcon(){
    let ret = 'fa-sort';
    let activeSortProperty = null;
    let desc = null;

    if (this.gridData.sort){
      desc = this.gridData.sort.charAt(0) == '-';
      activeSortProperty = desc ? this.gridData.sort.substring(1) : this.gridData.sort;
    }

    if (activeSortProperty == this.sortProperty){
      ret = desc ? 'fa-sort-desc' : 'fa-sort-asc';
    }

    return ret;
  }

  onButtonClick(){
    this.buttonClick.emit(this.sortProperty);
  }

  constructor() {

    if (!this.cssClass) {
      this.cssClass = "btn btn-primary btn-sm text-nowrap";
    }

  }

  ngOnInit() {

  }

}
