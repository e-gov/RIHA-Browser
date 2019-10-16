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
import {ActiveOrganizationChooserComponent} from "../active-organization-chooser/active-organization-chooser.component";
import {ModalHelperService} from "../../services/modal-helper.service";

@Component({
  selector: 'app-producer-organization',
  templateUrl: './producer-organization.component.html',
  styleUrls: ['./producer-organization.component.scss']
})
export class ProducerOrganizationComponent implements OnInit, DoCheck {

  gridData: GridData = new GridData();
  userMatrix: UserMatrix;
  loaded: boolean = false;
  differ: any;
  users: Array<User>;

  onPageChange(newPage): void{
    this.gridData.page = newPage - 1;
    this.getUsers(this.gridData.page);
  }

  onSortChange(property): void{
    this.gridData.changeSortOrder(property);
    this.getUsers(this.gridData.page);
  }

  getUsers(page?): void {
    if (this.userMatrix.isLoggedIn && this.userMatrix.isOrganizationSelected) {
      let params = {'sort': null, 'dir': null, 'page': null};
      let sortProperty = this.gridData.getSortProperty();
      if (sortProperty) {
        params.sort = sortProperty;
      }
      let sortOrder = this.gridData.getSortOrder();
      if (sortOrder) {
        params.dir = sortOrder;
      }
      if (page && page != 0) {
        params.page = page + 1;
      }

      let q = this.generalHelperService.generateQueryString(params);
      this.location.replaceState('/Minu/Organisatsioon', q);
      this.gridData.page = page || 0;

      this.systemsService.getOrganizationUsers(this.gridData).then(res => {
          this.gridData.updateData(res);
          if (this.gridData.getPageNumber() > 1 && this.gridData.getPageNumber() > this.gridData.totalPages) {
            this.getUsers();
          } else {
            this.loaded = true;
          }
        }, err => {
          this.loaded = true;
          this.toastrService.error('Serveri viga!');
        });
    }
  }

  openOrganizationsModal() {
    const modalRef = this.modalService.open(ActiveOrganizationChooserComponent);
    return false;
  }

  constructor(private environmentService: EnvironmentService,
              private generalHelperService: GeneralHelperService,
              private systemsService: SystemsService,
              private toastrService: ToastrService,
              private modalService: ModalHelperService,
              private route: ActivatedRoute,
              private location: Location,
              private differs: KeyValueDiffers) {
    this.differ = differs.find({}).create(null);
    this.userMatrix = this.environmentService.getUserMatrix();
  }

  ngOnInit() {
    this.route.queryParams.subscribe( params => {
      this.gridData.changeSortOrder(params['sort'] || 'lastName');
      this.gridData.setPageFromUrl(params['page']);
    });
    this.getUsers(this.gridData.page);
  }

  ngDoCheck() {
    var changes = this.differ.diff(this.environmentService.globalEnvironment);
    if (changes && (this.loaded || !this.userMatrix.isOrganizationSelected)){
      this.userMatrix = this.environmentService.getUserMatrix();
      this.getUsers();
    }
  }

}
