import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-date-row',
  templateUrl: './date-row.component.html',
  styleUrls: ['./date-row.component.scss'],
  standalone: false,
})
export class DateRowComponent implements OnInit {
  @Input() text: string;
  @Input() icon: string;
  @Input() date: string;

  formattedDate: string;

  constructor() {}

  ngOnInit() {}
}
