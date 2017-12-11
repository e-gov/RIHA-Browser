import {Component, OnInit, Input} from '@angular/core';
import { GridData } from '../../../models/grid-data';


@Component({
  selector: 'app-grid-currently-showing',
  templateUrl: './grid-currently-showing.component.html',
  styleUrls: ['./grid-currently-showing.component.scss']
})
export class GridCurrentlyShowingComponent implements OnInit {

  @Input() gridData: GridData;


  constructor() { }

  ngOnInit() {

  }

}
