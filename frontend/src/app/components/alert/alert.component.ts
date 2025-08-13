import { Component, OnInit, Input } from '@angular/core';

@Component({
    selector: 'app-alert',
    templateUrl: './alert.component.html',
    styleUrls: ['./alert.component.scss'],
    standalone: false
})
export class AlertComponent implements OnInit {

  @Input() conf;

  constructor() { }

  ngOnInit() {
  }

}
