import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { SystemsService } from '../../../services/systems.service';
import { System } from '../../../models/system';
import { Router } from '@angular/router';

@Component({
  selector: 'app-producer-edit-tech-docs',
  templateUrl: './producer-edit-tech-docs.component.html',
  styleUrls: ['./producer-edit-tech-docs.component.scss']
})
export class ProducerEditTechDocsComponent implements OnInit {

  @Input() system: System;
  @Input() tech_docs: any[];

  addTechDoc(urlInput, nameInput): void {
    if (urlInput.value.length > 0 && urlInput.checkValidity()){
      this.tech_docs.push({url: urlInput.value,
                          name: nameInput.value.trim()});
      urlInput.value = '';
      nameInput.value = '';
    }
  }

  deleteTechDoc(i): void {
    this.tech_docs.splice(i, 1);
  }

  saveSystem(){
    this.system.details.tech_docs = this.tech_docs;
    this.systemsService.updateSystem(this.system).then(response => {
      this.router.navigate(['/Kirjelda/Vaata/', response.json().id]);
    });
    this.activeModal.close('saved');
  }

  constructor(public activeModal: NgbActiveModal,
              private systemsService: SystemsService,
              private router: Router) { }

  ngOnInit() {
  }

}
