import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import { EnvironmentService } from '../../../services/environment.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { ModalHelperService } from '../../../services/modal-helper.service';
import { InfoModalComponent } from '../info-modal/info-modal.component';

@Component({
  selector: 'app-warning-modal',
  templateUrl: './warning-modal.component.html',
  styleUrls: ['./warning-modal.component.scss']
})
export class WarningModalComponent implements OnInit, OnDestroy {
  private static readonly ERROR_MESSAGE = 'Serveri viga.';

  @Input() timerStart;
  public minutesLeft: number;
  private timerId = null;

  private getMilisecondsLeft(){
    return this.environmentService.getSessionTimeoutInterval() - (new Date().getTime() - this.timerStart);
  }

  startCountdown(){
    this.timerId = setTimeout(() =>{
      const ml = this.getMilisecondsLeft();
      if (ml > 0) {
        this.minutesLeft = Math.floor((this.getMilisecondsLeft()/1000)/60);
        this.startCountdown();
      } else {
        this.forceLogout();
      }
    }, 1000)
  }

  forceLogout(){
    this.environmentService.doLogout().subscribe(
      res => {
        this.environmentService.loadEnvironmentData().subscribe(env => {
          this.modalService.open(InfoModalComponent);
          this.router.navigate(['/']);
        });
      }, err => {
        this.modalService.dismissAllModals();
        this.router.navigate(['/']);
        this.toastrService.error(WarningModalComponent.ERROR_MESSAGE);
      }
    )
  }

  doLogout(){
    this.environmentService.doLogout().subscribe(
      res => {
        this.environmentService.loadEnvironmentData().subscribe(env => {
          this.modalService.dismissAllModals();
          this.router.navigate(['/']);
        });
      }, err => {
        this.toastrService.error(WarningModalComponent.ERROR_MESSAGE);
      }
    )
  };

  refreshSession(){
    this.environmentService.loadEnvironmentData().subscribe(
      env => {
        this.modalService.closeActiveModal();
      }, err => {
        this.toastrService.error(WarningModalComponent.ERROR_MESSAGE);
      }
    )
  }

  constructor(private environmentService: EnvironmentService,
              private modalService: ModalHelperService,
              private toastrService: ToastrService,
              private router: Router) {
  }

  ngOnInit() {
    this.minutesLeft = Math.ceil((this.getMilisecondsLeft()/1000)/60);
    this.startCountdown();
  }

  ngOnDestroy(){
    clearTimeout(this.timerId);
  }

}
