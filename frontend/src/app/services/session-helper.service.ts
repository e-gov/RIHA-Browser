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
          const modalRef = this.modalService.open(WarningModalComponent, {
            size: "sm",
            backdrop: "static",
            keyboard: false
          }, true);
          modalRef.componentInstance.timerStart = this.timerStart;
        }
      }, this.environmentService.getSessionTimeoutInterval() - (6*60*1000));
    }
  }

  constructor(private modalService: ModalHelperService,
              private environmentService: EnvironmentService) { }

}
