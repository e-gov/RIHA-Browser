import {Component, Input, OnInit} from '@angular/core';
import {classifiers} from "../../../services/environment.service";

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

  classifiers = classifiers;

  constructor() { }

  ngOnInit() {
  }

}
