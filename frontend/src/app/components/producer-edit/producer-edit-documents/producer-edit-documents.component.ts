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
  isChanged: boolean = false;

  data: any = {url: '', name: ''};

  addTechDoc(addForm): void {
    if (addForm.valid){
      this.documents.push({url: this.data.url,
                          name: this.data.name ? this.data.name.trim() : ''});
      this.data = {url: '', name: ''};
      addForm.reset();
      this.isChanged = true;
    }
  }

  deleteTechDoc(i): void {
    this.documents.splice(i, 1);
    this.isChanged = true;
  }

  saveSystem(){
    this.system.details.documents = this.documents;
    this.systemsService.updateSystem(this.system).then(response => {
      this.router.navigate(['/Kirjelda/Vaata/', response.json().details.short_name]);
    });
    this.activeModal.close('saved');
  }

  closeModal(f){
    if (this.isChanged || f.form.dirty){
      if (confirm('Oled väljades muudatusi teinud. Kui navigeerid siit ära ilma salvestamata, siis sinu muudatused kaovad.')){
        this.activeModal.close();
      } else {
        return false;
      }
    } else {
      this.activeModal.close();
    }
  }

  constructor(private activeModal: NgbActiveModal,
              private systemsService: SystemsService,
              private router: Router) { }

  ngOnInit() {
  }

}
