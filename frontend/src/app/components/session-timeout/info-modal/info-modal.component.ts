import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ModalHelperService } from '../../../services/modal-helper.service';

@Component({
  selector: 'app-info-modal',
  templateUrl: './info-modal.component.html',
  styleUrls: ['./info-modal.component.scss']
})
export class InfoModalComponent implements OnInit {

  goToLogin(){
    this.modalService.dismissAllModals();
    this.router.navigate(['/Login']);
    return false;
  }

  constructor(private router: Router,
              private modalService: ModalHelperService) { }

  ngOnInit() {
  }

}
