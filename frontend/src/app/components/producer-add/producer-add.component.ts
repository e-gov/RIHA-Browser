import { Component, OnInit, DoCheck, KeyValueDiffers  } from '@angular/core';
import { Router } from '@angular/router';
import { SystemsService } from '../../services/systems.service';
import { EnvironmentService} from "../../services/environment.service";
import { ModalHelperService } from '../../services/modal-helper.service';
import { ActiveOrganizationChooserComponent } from '../active-organization-chooser/active-organization-chooser.component';
import { UserMatrix } from '../../models/user-matrix';
import { GeneralHelperService } from '../../services/general-helper.service';

@Component({
  selector: 'app-producer-add',
  templateUrl: './producer-add.component.html',
  styleUrls: ['./producer-add.component.scss']
})
export class ProducerAddComponent implements OnInit, DoCheck {

  userMatrix: UserMatrix;
  alertConf: any = null;
  timeoutId: any = null;
  differ: any;

  onSubmit(f) :void {
    this.alertConf = null;
    if (f.valid){
      this.systemsService.addSystem(f.value).subscribe(
        responseSystem => {
          this.router.navigate(['/Kirjelda/Vaata', responseSystem.details.short_name]);
        }, err => {
          this.alertConf = {
            type: 'danger',
            heading: 'Viga',
            text: this.systemsService.getAlertText(err)
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
              private generalHelperService: GeneralHelperService,
              private differs: KeyValueDiffers,
              private modalService: ModalHelperService) {
    this.differ = differs.find({}).create(null);
    this.userMatrix = this.environmentService.getUserMatrix();
  }

  ngOnInit() {
    this.generalHelperService.setRihaPageTitle('Sisesta infosüsteemi andmed');
  }

  ngDoCheck() {
    var changes = this.differ.diff(this.environmentService.globalEnvironment);
    if (changes){
      this.userMatrix = this.environmentService.getUserMatrix();
    }
  }

}
