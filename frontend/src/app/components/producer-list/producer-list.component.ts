import { Component, OnInit } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { EnvironmentService } from '../../services/environment.service';
import { GridData } from '../../models/grid-data';
import { UserMatrix } from '../../models/user-matrix';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActiveOrganizationChooserComponent } from '../active-organization-chooser/active-organization-chooser.component';
import { G } from '../../globals/globals';

@Component({
  selector: 'app-producer-list',
  templateUrl: './producer-list.component.html',
  styleUrls: ['./producer-list.component.scss']
})
export class ProducerListComponent implements OnInit {

  gridData: GridData  = new GridData();
  filters: {
    name: string,
    shortName: string
  };
  userMatrix: UserMatrix;

  onPageChange(newPage): void{
    this.gridData.page = newPage - 1;
    this.getOwnSystems();
  }

  onSortChange(property): void{
    this.gridData.changeSortOrder(property);
    this.getOwnSystems();
  }

  getOwnSystems(): void {
    if (this.userMatrix.isLoggedIn && this.userMatrix.isOrganizationSelected){
      this.systemsService.getOwnSystems(this.filters, this.gridData).then(
      res => {
        this.gridData.updateData(res.json());
      })
    }
  }

  openOrganizationsModal() {
    const modalRef = this.modalService.open(ActiveOrganizationChooserComponent);
    return false;
  }

  getSystemStatusText(system){
    let statusDescription = 'määramata';
    if (system.details.meta && system.details.meta.system_status) {
      let status = system.details.meta.system_status.status;
      switch (status) {
        case G.system_status.IN_USE: {
          statusDescription = 'kasutusel';
          break;
        }
        case G.system_status.ESTABLISHING: {
          statusDescription = 'asutamisel';
          break;
        }
        case G.system_status.FINISHED: {
          statusDescription = 'lõpetatud';
          break
        }
      }
    }
    return statusDescription;
  }

  constructor(private systemsService: SystemsService,
              private environmentService: EnvironmentService,
              private modalService: NgbModal) {
    this.filters = {
      name: null,
      shortName: null
    };
    this.userMatrix = this.environmentService.getUserMatrix();
  }

  ngOnInit() {
    this.gridData.changeSortOrder('meta.update_timestamp');
    this.getOwnSystems();
  }

}
