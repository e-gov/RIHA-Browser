import {Component, Input} from '@angular/core';
import {SystemsService} from '../../../services/systems.service';
import {System} from '../../../models/system';
import {ModalHelperService} from '../../../services/modal-helper.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-producer-edit-standard-realisations',
  templateUrl: './producer-edit-standard-realisations.component.html',
  styleUrls: ['./producer-edit-standard-realisations.component.scss']
})
export class ProducerEditStandardRealisationsComponent {

  @Input() system: System;

  alertConf: any = null;
  timeoutId: any = null;

  createStandardRealisationSystem(addForm){
    if (addForm.valid){
      this.systemsService.createStandardRealisationSystem(
        this.system.details.short_name, addForm.value).subscribe(responseSystem => {
        this.closeModal();
        this.router.navigate(['/Infosüsteemid/Vaata', responseSystem.details.short_name]);
      }, err => {

        let errJson = null;
        try {
          errJson = err;
        } catch (e) {
          //ignored
        }

        this.alertConf = {
          type: 'danger',
          heading: 'Viga',
          text: errJson? this.systemsService.getAlertText(errJson) : 'Üldine viga'
        };
        clearTimeout(this.timeoutId);
        this.timeoutId = setTimeout(()=>{
          this.alertConf = null
        }, 10000);

      });
    }
  };

  closeModal(){
    this.modalService.closeActiveModal();
  };

  constructor(private systemsService: SystemsService,
              private router: Router,
              private modalService: ModalHelperService) { }



}
