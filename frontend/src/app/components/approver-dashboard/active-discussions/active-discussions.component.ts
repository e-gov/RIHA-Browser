import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-active-discussions',
  templateUrl: './active-discussions.component.html',
  styleUrls: ['./active-discussions.component.scss'],
  standalone: false,
})
export class ActiveDiscussionsComponent implements OnInit {
  active = 1;

  constructor() {}

  ngOnInit() {}
}
