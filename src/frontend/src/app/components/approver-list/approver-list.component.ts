import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-approver-list',
  templateUrl: './approver-list.component.html',
  styleUrls: ['./approver-list.component.scss']
})
export class ApproverListComponent implements OnInit {

  systems: any[];

  constructor() {
    this.systems = [];
  }

  ngOnInit() {
  }

}
