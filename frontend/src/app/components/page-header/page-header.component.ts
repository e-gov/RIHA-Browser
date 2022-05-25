import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-page-header',
  templateUrl: './page-header.component.html'
})
export class PageHeaderComponent implements OnInit {

  @Input() text: string;

  @Input() subHeader: string;

  constructor() {
  }

  ngOnInit() {
  }

}
