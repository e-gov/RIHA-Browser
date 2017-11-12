import { Component, OnInit } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { EnvironmentService } from '../../services/environment.service';
import { GridData } from '../../models/grid-data';
import { UserMatrix } from '../../models/user-matrix';
import { ModalHelperService } from '../../services/modal-helper.service';
import { ActiveOrganizationChooserComponent } from '../active-organization-chooser/active-organization-chooser.component';
import { GeneralHelperService } from '../../services/general-helper.service';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-producer-list',
  templateUrl: './producer-list.component.html',
  styleUrls: ['./producer-list.component.scss']
})
export class ProducerListComponent implements OnInit {

  gridData: GridData  = new GridData();
  filters: {
    name: string,
    shortName: string,
    topic: string
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
      let q = this.generalHelperService.generateQueryString({name: this.filters.name,
                                                                  shortName: this.filters.shortName,
                                                                  topic: this.filters.topic});
      this.location.replaceState('/Kirjelda', q);
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

  constructor(private systemsService: SystemsService,
              private environmentService: EnvironmentService,
              private route: ActivatedRoute,
              private location: Location,
              public  generalHelperService: GeneralHelperService,
              private modalService: ModalHelperService) {
    this.userMatrix = this.environmentService.getUserMatrix();
  }

  ngOnInit() {
    this.route.queryParams.subscribe( params => {
      this.filters = {
        shortName: params['shortName'],
        name: params['name'],
        topic: params['topic']
      };
    });

    this.gridData.changeSortOrder('meta.update_timestamp', 'DESC');
    this.getOwnSystems();
  }

}
