import { Component, OnInit, Input } from '@angular/core';
import { GridData } from '../../../models/grid-data';

@Component({
  selector: 'app-grid-total-found',
  templateUrl: './grid-total-found.component.html',
  styleUrls: ['./grid-total-found.component.scss']
})
export class GridTotalFoundComponent implements OnInit {

  @Input() gridData: GridData;

  constructor() { }

  ngOnInit() {
  }

}
