import { Injectable } from '@angular/core';
import { ModalHelperService } from './modal-helper.service';
import { WarningModalComponent } from './../components/session-timeout/warning-modal/warning-modal.component';
import { EnvironmentService } from './environment.service';

@Injectable()
export class SessionHelperService {

  private sessionTimerId = null;
  private timerStart;

  public refreshSessionTimer(): void{
    if (this.sessionTimerId){
      clearTimeout(this.sessionTimerId);
    }
    if (this.environmentService.getActiveUser()){
      this.timerStart = new Date().getTime();
      this.sessionTimerId = setTimeout(()=> {
        if (this.environmentService.getActiveUser()){
          this.modalService.dismissActiveModal();
          const modalRef = this.modalService.open(WarningModalComponent, {
            size: "sm",
            backdrop: "static",
            keyboard: false
          });
          modalRef.componentInstance.timerStart = this.timerStart;
        }
      }, this.environmentService.getSessionTimeoutInterval() - (5*60*1000));
    }
  }

  constructor(private modalService: ModalHelperService,
              private environmentService: EnvironmentService) { }

}
