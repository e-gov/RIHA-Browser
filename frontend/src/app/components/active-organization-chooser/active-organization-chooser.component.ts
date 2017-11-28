import { Component, OnInit } from '@angular/core';
import { EnvironmentService } from '../../services/environment.service';
import { User } from '../../models/user';
import { Router } from '@angular/router';
import { Environment } from '../../models/environment';
import { ModalHelperService } from '../../services/modal-helper.service';

@Component({
  selector: 'app-active-organization-chooser',
  templateUrl: './active-organization-chooser.component.html',
  styleUrls: ['./active-organization-chooser.component.scss']
})
export class ActiveOrganizationChooserComponent implements OnInit {
  user: User;

  selectOrganization(organizationCode): void {
    this.environmentService.setActiveOrganization(organizationCode).then(res => {
      this.environmentService.globalEnvironment = new Environment(res.json());
      this.router.navigate(['/']);
      this.modalService.closeActiveModal();
    });
  }

  constructor(private environmentService: EnvironmentService,
              private modalService: ModalHelperService,
              private router: Router) {
    this.user = environmentService.getActiveUser();
  }

  ngOnInit() {
  }

}
