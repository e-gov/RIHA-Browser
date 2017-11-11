import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SystemsService } from '../../services/systems.service';
import { EnvironmentService} from "../../services/environment.service";
import { Location } from '@angular/common';
import { ToastrService } from "ngx-toastr";
import { ModalHelperService } from '../../services/modal-helper.service';
import { ActiveOrganizationChooserComponent } from '../active-organization-chooser/active-organization-chooser.component';
import { UserMatrix } from '../../models/user-matrix';

@Component({
  selector: 'app-producer-add',
  templateUrl: './producer-add.component.html',
  styleUrls: ['./producer-add.component.scss']
})
export class ProducerAddComponent implements OnInit {

  userMatrix: UserMatrix;
  alertConf: any = null;
  timeoutId: any = null;

  onSubmit(f) :void {
    this.alertConf = null;
    if (f.valid){
      this.systemsService.addSystem(f.value).then(
        res => {
          this.router.navigate(['/Kirjelda/Vaata', res.json().details.short_name]);
        }, err => {
          this.alertConf = {
            type: 'danger',
            heading: 'Viga',
            text: this.systemsService.getAlertText(err.json())
          };
          clearTimeout(this.timeoutId);
          this.timeoutId = setTimeout(()=>{
            this.alertConf = null
          }, 10000);
        })
    }
  }

  openOrganizationsModal() {
    const modalRef = this.modalService.open(ActiveOrganizationChooserComponent);
    return false;
  }

  constructor(private systemsService: SystemsService,
              private environmentService: EnvironmentService,
              private router: Router,
              private location: Location,
              private toastrService: ToastrService,
              private modalService: ModalHelperService) {
    this.userMatrix = this.environmentService.getUserMatrix();
  }

  ngOnInit() {
  }

}
