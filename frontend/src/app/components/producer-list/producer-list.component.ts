import { Component, OnInit } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { GridData } from '../../models/grid-data';
import { isNumber } from "util";

@Component({
  selector: 'app-producer-list',
  templateUrl: './producer-list.component.html',
  styleUrls: ['./producer-list.component.scss']
})
export class ProducerListComponent implements OnInit {

  gridData: GridData;
  filters: {
    name: string,
    shortName: string
  }

  onPageChange(newPage){
    this.gridData.page = newPage - 1;
    this.getOwnSystems();
  }

  getOwnSystems(): void {
    this.systemsService.getOwnSystems(this.filters, this.gridData.page).then(
      res => {
        this.gridData.updateData(res.json());
      })
  }

  constructor(private systemsService: SystemsService) {
    this.gridData = new GridData();
    this.filters = {
      name: null,
      shortName: null
    }
  }

  ngOnInit() {
    this.getOwnSystems();
  }

}
