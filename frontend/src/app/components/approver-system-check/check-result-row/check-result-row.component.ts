import {Component, Input, OnInit} from '@angular/core';
import {globals} from "../../../services/environment.service";

@Component({
  selector: 'app-check-result-row',
  templateUrl: './check-result-row.component.html',
  styleUrls: ['./check-result-row.component.scss']
})
export class CheckResultRowComponent implements OnInit {

  @Input() systemCheckStatus: any;
  @Input() text: string;
  @Input() successText: string;
  @Input() failText: string;

  globals = globals;

  constructor() { }

  ngOnInit() {
  }

}
