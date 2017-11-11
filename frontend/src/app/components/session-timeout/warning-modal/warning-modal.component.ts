import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import { EnvironmentService } from '../../../services/environment.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalHelperService } from '../../../services/modal-helper.service';
import { InfoModalComponent } from '../info-modal/info-modal.component';

@Component({
  selector: 'app-warning-modal',
  templateUrl: './warning-modal.component.html',
  styleUrls: ['./warning-modal.component.scss']
})
export class WarningModalComponent implements OnInit, OnDestroy {

  @Input() timerStart;
  private minutesLeft: number;
  private timerId = null;

  private getMilisecondsLeft(){
    return this.environmentService.getSessionTimeoutInterval() - (new Date().getTime() - this.timerStart);
  }

  startCountdown(){
    this.timerId = setTimeout(() =>{
      let ml = this.getMilisecondsLeft();
      if (ml > 0) {
        this.minutesLeft = Math.ceil((this.getMilisecondsLeft()/1000)/60);
        this.startCountdown();
      } else {
        this.forceLogout();
      }
    }, 1000)
  }

  forceLogout(){
    this.environmentService.doLogout().then(
      res => {
        this.environmentService.loadEnvironmentData().then(env => {
          this.activeModal.dismiss();
          this.modalService.open(InfoModalComponent);
        });
      }, err => {
        this.toastrService.error('Serveri viga.');
      }
    )
  }

  doLogout(){
    this.environmentService.doLogout().then(
      res => {
        this.environmentService.loadEnvironmentData().then(env => {
          this.activeModal.dismiss();
          this.router.navigate(['/']);
        });
      }, err => {
        this.toastrService.error('Serveri viga.');
      }
    )
  };

  refreshSession(){
    this.environmentService.loadEnvironmentData().then(
      res => {
        this.activeModal.close();
      }, err => {
        this.toastrService.error('Serveri viga.');
      }
    )
  }

  constructor(private environmentService: EnvironmentService,
              private modalService: ModalHelperService,
              private activeModal: NgbActiveModal,
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
