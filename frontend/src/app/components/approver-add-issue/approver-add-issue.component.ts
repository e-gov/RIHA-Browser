import { Component, OnInit, Input } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { EnvironmentService } from '../../services/environment.service';
import { System } from '../../models/system';
import { ToastrService } from 'ngx-toastr';
import { User } from '../../models/user';
import { ModalHelperService } from '../../services/modal-helper.service';

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
          this.modalService.closeActiveModal();
        },
        err => {
          this.toastrService.error('Serveri viga! Proovige uuesti!');
        })
    }
  }

  closeModal(f){
    if (f.form.dirty){
      if (confirm('Oled sisestanud väljadesse infot. Kui navigeerid siit ära ilma salvestamata, siis sinu sisestatud info kaob.')){
        this.modalService.dismissActiveModal();
      } else {
        return false;
      }
    } else {
      this.modalService.dismissActiveModal();
    }
  }

  constructor(private modalService: ModalHelperService,
              private systemsService: SystemsService,
              private toastrService: ToastrService,
              private environmentService: EnvironmentService) {
    this.activeUser = this.environmentService.getActiveUser();
  }

  ngOnInit() {
  }

}
