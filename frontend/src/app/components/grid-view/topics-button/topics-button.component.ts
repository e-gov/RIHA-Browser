import {Component, Input, OnInit} from '@angular/core';
import {GridData} from "../../../models/grid-data";

@Component({
  selector: 'app-topics-button',
  templateUrl: './topics-button.component.html',
  styleUrls: ['./topics-button.component.scss']
})
export class TopicsButtonComponent implements OnInit {

  @Input() labelText: string;
  @Input() gridData: GridData;

  constructor() {
  }

  ngOnInit(): void {
  }

}
