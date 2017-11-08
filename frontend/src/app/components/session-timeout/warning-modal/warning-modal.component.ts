import { Component, OnInit } from '@angular/core';
import { EnvironmentService } from '../../../services/environment.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { InfoModalComponent } from '../info-modal/info-modal.component';

@Component({
  selector: 'app-warning-modal',
  templateUrl: './warning-modal.component.html',
  styleUrls: ['./warning-modal.component.scss']
})
export class WarningModalComponent implements OnInit {

  minutesLeft: number = 5;

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
              private modalService: NgbModal,
              private activeModal: NgbActiveModal,
              private toastrService: ToastrService,
              private router: Router) {
    setTimeout(()=> {this.forceLogout()},10000);
  }

  ngOnInit() {
  }

}
