import { Component, OnInit } from '@angular/core';
import { EnvironmentService } from '../../services/environment.service';
import { User } from '../../models/user';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-active-organization-chooser',
  templateUrl: './active-organization-chooser.component.html',
  styleUrls: ['./active-organization-chooser.component.scss']
})
export class ActiveOrganizationChooserComponent implements OnInit {
  user: User;

  selectOrganization(id): void {
    this.activeModal.close();
  }

  constructor(private environmentService: EnvironmentService,
              private activeModal: NgbActiveModal) {
    this.user = environmentService.getActiveUser();
  }

  ngOnInit() {
  }

}
