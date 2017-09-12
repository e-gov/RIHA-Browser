import { Component, OnInit } from '@angular/core';
import { EnvironmentService } from '../../services/environment.service';
import { User } from '../../models/user';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { Environment } from '../../models/environment';

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
      this.activeModal.close();
    });
  }

  constructor(private environmentService: EnvironmentService,
              private activeModal: NgbActiveModal,
              private router: Router) {
    this.user = environmentService.getActiveUser();
  }

  ngOnInit() {
  }

}
