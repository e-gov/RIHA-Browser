import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { SystemsService } from '../../services/systems.service';
import { System } from '../../models/system';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-approver-add-comment',
  templateUrl: './approver-add-comment.component.html',
  styleUrls: ['./approver-add-comment.component.scss']
})
export class ApproverAddCommentComponent implements OnInit {

  @Input() system: System;

  onSubmit(f) :void {
    if (f.valid){
      this.systemsService.addSystemComment(this.system.details.uuid, f.value).then(
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
              private toastrService: ToastrService) { }

  ngOnInit() {
  }

}
