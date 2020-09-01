import {Component, DoCheck, HostListener, KeyValueDiffers, OnInit} from '@angular/core';
import {SystemsService} from '../../services/systems.service';
import {EnvironmentService} from "../../services/environment.service";
import {ActivatedRoute, Router} from '@angular/router';
import {System} from '../../models/system';
import {User} from '../../models/user';
import {ToastrService} from 'ngx-toastr';
import {UserMatrix} from '../../models/user-matrix';
import {GeneralHelperService} from '../../services/general-helper.service';
import {CanDeactivateModal} from "../../guards/can-deactivate-modal.guard";
import {ModalHelperService} from "../../services/modal-helper.service";

@Component({
  selector: 'app-producer-details',
  templateUrl: './producer-details.component.html',
  styleUrls: ['./producer-details.component.scss']
})
export class ProducerDetailsComponent implements OnInit, DoCheck, CanDeactivateModal {
  private system: System = new System();
  private user: User;
  public loaded: boolean;
  public notFound: boolean;
  public issueId: any;
  public userMatrix: UserMatrix;
  private differ: any;

  // canDeactivate guard handler
  canDeactivate() {
    const modal = this.modalService.lastModal;
    if (modal) {
      const component = modal && modal.componentInstance ? modal.componentInstance : null;
      if (component && 'canDeactivate' in component) {
        return component.canDeactivate()
      }

      return true;
    }

    return true;
  }

  isEditingAllowed(){
    const user = this.environmentService.getActiveUser();
    let editable = false;
    if (user) {
      editable = user.canEdit(this.system.getOwnerCode());
    }
    return editable;
  }

  isBlockVisible(blockName){
    const editable = this.isEditingAllowed();
    let ret = false;
    switch (blockName) {
      case 'legislations': {
        ret = this.system.hasLegislations() || editable;
        break;
      }
      case 'documents': {
        ret = this.system.hasDocuments() || editable;
        break;
      }
      case 'dataObjects': {
        ret = this.system.hasDataObjects() || this.system.hasDataFiles() || editable;
        break;
      }
      case 'contacts': {
        ret = (this.system.hasContacts() && typeof this.environmentService.getActiveUser() !== 'undefined' && this.environmentService.getActiveUser() != null) || editable;
        break;
      }
      case 'security': {
        ret = this.system.hasSecurityInfo() || editable;
        break;
      }
    }
    return ret;
  }

  onIssueResolve(issueType){
    if (issueType){
      this.getSystem(this.system.details.short_name);
    }
  }

  onRelationsRefresh(relations) {
    if (!relations) {
      return;
    }

    this.getSystem(this.system.details.short_name);
  }

  onIssueError(error){
    if (error.status == '404'){
      this.loaded = false;
      this.notFound = true;
    }
  }

  isLoginErrorVisible(){
    this.userMatrix = this.environmentService.getUserMatrix();
    return this.issueId && !this.userMatrix.isLoggedIn;
  }

  isCannotViewCommentsErrorVisible(){
    this.userMatrix = this.environmentService.getUserMatrix();
    if (this.loaded && this.userMatrix.isLoggedIn){
      const user = this.environmentService.getActiveUser();
      return this.issueId && this.userMatrix.isLoggedIn && user && !(user.canEdit(this.system.getOwnerCode()) || this.userMatrix.hasApproverRole);
    } else {
      return false;
    }
  }

  getSystem(reference){
    this.systemsService.getSystem(reference).subscribe(responseSystem => {
      this.system = new System(responseSystem);
      this.generalHelperService.setRihaPageTitle(this.system.details.name);
      this.loaded = true;
      setTimeout(()=>{this.generalHelperService.adjustSection(this.issueId ? '#tagasiside' : null)}, 0);
    }, err => {
      const status = err.status;
      if (status == '404'){
        this.notFound = true;
        this.generalHelperService.setRihaPageTitle('Lehekülge ei leitud');
      } else if (status == '500'){
        this.toastrService.error('Serveri viga');
        this.router.navigate(['/']);
      } else {
        this.router.navigate(['/']);
      }
    });
  }

  constructor(private differs: KeyValueDiffers,
              private systemsService: SystemsService,
              private environmentService: EnvironmentService,
              public generalHelperService: GeneralHelperService,
              private route: ActivatedRoute,
              private router: Router,
              private toastrService: ToastrService,
              private modalService: ModalHelperService) {
    this.differ = differs.find({}).create();
    this.userMatrix = this.environmentService.getUserMatrix();
  }

  ngOnInit() {
    this.route.params.subscribe( params => {
      this.loaded = false;
      this.notFound = false;
      this.issueId = params['issue_id'] || null;
      this.getSystem(params['reference']);
    });
    this.generalHelperService.setRihaPageTitle();
  }

  @HostListener("window:scroll", [])
  onWindowScroll() {

  }

  ngDoCheck() {
    const changes = this.differ.diff(this.environmentService.globalEnvironment);
    if (changes && (this.loaded || !this.userMatrix.isOrganizationSelected)){
      this.userMatrix = this.environmentService.getUserMatrix();
    }
  }
}
