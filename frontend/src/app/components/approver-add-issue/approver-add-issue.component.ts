import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { SystemsService } from '../../services/systems.service';
import { EnvironmentService } from '../../services/environment.service';
import { System } from '../../models/system';
import { ToastrService } from 'ngx-toastr';
import { User } from '../../models/user';

@Component({
  selector: 'app-approver-add-comment',
  templateUrl: './approver-add-issue.component.html',
  styleUrls: ['./approver-add-issue.component.scss']
})
export class ApproverAddIssueComponent implements OnInit {

  @Input() system: System;
  activeUser: User;

  onSubmit(f) :void {
    if (f.valid){
      this.systemsService.addSystemIssue(this.system.details.short_name, f.value).then(
        res => {
          this.activeModal.close();
        },
        err => {
          this.toastrService.error('Serveri viga! Proovige uuesti!');
        })
    }
  }

  constructor(public activeModal: NgbActiveModal,
              private systemsService: SystemsService,
              private toastrService: ToastrService,
              private environmentService: EnvironmentService) {
    this.activeUser = this.environmentService.getActiveUser();
  }

  ngOnInit() {
  }

}
