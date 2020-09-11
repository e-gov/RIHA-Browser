import {Component, DoCheck, KeyValueDiffers, OnInit} from '@angular/core';
import {GridData} from "../../models/grid-data";
import {UserMatrix} from "../../models/user-matrix";
import {EnvironmentService} from "../../services/environment.service";
import {ActivatedRoute} from "@angular/router";
import {GeneralHelperService} from "../../services/general-helper.service";
import {Location} from "@angular/common";
import {SystemsService} from "../../services/systems.service";
import {ToastrService} from "ngx-toastr";
import {User} from "../../models/user";
import {ModalHelperService} from "../../services/modal-helper.service";
import {ActiveOrganizationChooserComponent} from '../active-organization-chooser/active-organization-chooser.component';

@Component({
  selector: 'app-producer-dashboard',
  templateUrl: './producer-dashboard.component.html',
  styleUrls: ['./producer-dashboard.component.scss']
})
export class ProducerDashboardComponent implements OnInit, DoCheck {

  public userMatrix: UserMatrix;
  public loaded: boolean = false;
  private differ: any;
  public gridData: GridData = new GridData();

  public onSortChange(property): void{
    this.gridData.changeSortOrder(property);
    this.getOwnOpenIssues();
  }

  private getOwnOpenIssues(){
    if (this.userMatrix.isLoggedIn && this.userMatrix.isOrganizationSelected && this.environmentService.getActiveUser()) {
      this.systemsService.getActiveIssuesForOrganization(this.environmentService.getActiveUser().activeOrganization.code, this.gridData.sort).subscribe(res =>{
        this.gridData.updateData(res);
        this.loaded = true;
      }, err => {
        this.helper.showError();
        this.loaded = true;
      });
    }
  }
  
  openOrganizationsModal() {
    const modalRef = this.modalService.open(ActiveOrganizationChooserComponent);
    return false;
  }

  constructor(private environmentService: EnvironmentService,
              private helper: GeneralHelperService,
              private systemsService: SystemsService,
              private toastrService: ToastrService,
              private modalService: ModalHelperService,
              private route: ActivatedRoute,
              private location: Location,
              private differs: KeyValueDiffers) {
    this.differ = differs.find({}).create();
    this.userMatrix = this.environmentService.getUserMatrix();
  }

  ngOnInit() {
    this.helper.setRihaPageTitle('Minu arutelud');
    this.gridData.changeSortOrder('latest_interaction');
    this.getOwnOpenIssues();
  }

  ngDoCheck() {
    const changes = this.differ.diff(this.environmentService.globalEnvironment);
    if (changes && (this.loaded || !this.userMatrix.isOrganizationSelected)){
      this.userMatrix = this.environmentService.getUserMatrix();
      this.getOwnOpenIssues();
    }
  }

}
