import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-systems-for-approval-list',
  templateUrl: './systems-for-approval-list.component.html',
  styleUrls: ['./systems-for-approval-list.component.scss']
})
export class SystemsForApprovalListComponent implements OnInit {
  public loaded: boolean = false;

  constructor() { }

  ngOnInit() {
  }

}
