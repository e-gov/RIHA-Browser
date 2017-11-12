import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-producer-edit-relations',
  templateUrl: './producer-edit-relations.component.html',
  styleUrls: ['./producer-edit-relations.component.scss']
})
export class ProducerEditRelationsComponent implements OnInit {

  relation: {
    systemId: string,
    relationType: any
  };
  relations: any[] = [];

  addRelation(){};
  deleteRelation(){};
  closeModal(){};

  constructor() { }

  ngOnInit() {
  }

}
