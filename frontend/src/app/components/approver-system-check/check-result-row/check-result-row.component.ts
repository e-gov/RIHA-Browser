import { Component, OnInit, Input } from '@angular/core';
import { G } from '../../../globals/globals';

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

  public globals: any = G;

  constructor() { }

  ngOnInit() {
  }

}
