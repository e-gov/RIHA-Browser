import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-info-modal',
  templateUrl: './info-modal.component.html',
  styleUrls: ['./info-modal.component.scss']
})
export class InfoModalComponent implements OnInit {

  goToLogin(){
    this.activeModal.dismiss();
    this.router.navigate(['/Login']);
    return false;
  }

  constructor(private router: Router,
              private activeModal: NgbActiveModal) { }

  ngOnInit() {
  }

}
