import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SystemsService } from '../../services/systems.service';
import { EnvironmentService} from "../../services/environment.service";
import { Location } from '@angular/common';
import { ToastrService } from "ngx-toastr";
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActiveOrganizationChooserComponent } from '../active-organization-chooser/active-organization-chooser.component';
import { UserMatrix } from '../../models/user-matrix';



@Component({
  selector: 'app-producer-add',
  templateUrl: './producer-add.component.html',
  styleUrls: ['./producer-add.component.scss']
})
export class ProducerAddComponent implements OnInit {

  userMatrix: UserMatrix;

  onSubmit(f) :void {
    if (f.valid){
      this.systemsService.addSystem(f.value).then(
        res => {
          this.router.navigate(['/Kirjelda/Vaata', res.json().details.short_name]);
        }, err => {
          this.toastrService.error('Infosüsteemi lisamine ebaõnnestus');
          this.location.back();
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
              private modalService: NgbModal) {
    this.userMatrix = this.environmentService.getUserMatrix();
  }

  ngOnInit() {
  }

}
