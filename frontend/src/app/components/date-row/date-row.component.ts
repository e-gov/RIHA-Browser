import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-date-row',
  templateUrl: './date-row.component.html',
  styleUrls: ['./date-row.component.scss']
})
export class DateRowComponent implements OnInit {

  @Input() text: string;
  @Input() icon: string;
  @Input() date: string;

  formattedDate: string;

  constructor() {
  }

  ngOnInit() {
  }

}
