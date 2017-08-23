import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { SystemsService } from '../../../services/systems.service';
import { System } from '../../../models/system';
import { Router } from '@angular/router';

@Component({
  selector: 'app-producer-edit-tech-docs',
  templateUrl: './producer-edit-documents.component.html',
  styleUrls: ['./producer-edit-documents.component.scss']
})
export class ProducerEditDocumentsComponent implements OnInit {

  @Input() system: System;
  @Input() documents: any[];

  addTechDoc(urlInput, nameInput): void {
    if (urlInput.value.length > 0 && urlInput.checkValidity()){
      this.documents.push({url: urlInput.value,
                          name: nameInput.value.trim()});
      urlInput.value = '';
      nameInput.value = '';
    }
  }

  deleteTechDoc(i): void {
    this.documents.splice(i, 1);
  }

  saveSystem(){
    this.system.details.documents = this.documents;
    this.systemsService.updateSystem(this.system).then(response => {
      this.router.navigate(['/Kirjelda/Vaata/', response.json().details.short_name]);
    });
    this.activeModal.close('saved');
  }

  constructor(public activeModal: NgbActiveModal,
              private systemsService: SystemsService,
              private router: Router) { }

  ngOnInit() {
  }

}
