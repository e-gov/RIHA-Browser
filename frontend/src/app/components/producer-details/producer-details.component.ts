import { Component, OnInit, HostListener } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { EnvironmentService } from "../../services/environment.service";
import { ActivatedRoute } from '@angular/router';
import { System } from '../../models/system';
import { User } from '../../models/user';
import { WindowRefService } from '../../services/window-ref.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

declare var $: any;

@Component({
  selector: 'app-producer-details',
  templateUrl: './producer-details.component.html',
  styleUrls: ['./producer-details.component.scss']
})
export class ProducerDetailsComponent implements OnInit {
  private system: System = new System();
  private user: User;
  public loaded: boolean;
  public notFound: boolean;
  public issueId: any;

  adjustSection(hash?){
    hash = hash || this.winRef.nativeWindow.location.hash;
    if (hash){
      let elId = decodeURI(hash.replace('#',''));
      let el = $(hash)[0];
      if (el){
        this.winRef.nativeWindow.scrollTo(0,$(el).offset().top);
      }
    }
  }

  isMenuActive(blockId, first?){
    let element = $(`#${blockId}`)[0];
    if (element){
      let yOffset = $(element).offset().top - $(document).scrollTop();
      let height = element.offsetHeight;
      if (first === true) {
        return yOffset + height > 0;
      } else {
        return yOffset <= 0 && (yOffset + height > 0)
      }
    } else {
      return false
    }
  }

  isEditingAllowed(){
    let editable = false;
    let user = this.environmentService.getActiveUser();
    if (user) {
      editable = user.canEdit(this.system.getOwnerCode());
    }
    return editable;
  }

  isBlockVisible(blockName){
    let editable = this.isEditingAllowed();
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
        ret = (this.system.hasContacts() && this.user != null) || editable;
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

  onIssueError(error){
    if (error.status == '404'){
      this.loaded = false;
      this.notFound = true;
    }
  }

  getSystem(id){
    this.systemsService.getSystem(id).then(response => {
      this.system = new System(response.json());
      this.loaded = true;
      setTimeout(()=>{this.adjustSection(this.issueId ? '#tagasiside' : null)}, 0);
    }, err => {
      let status = err.status;
      if (status == '404'){
        this.notFound = true;
      } else if (status == '500'){
        this.toastrService.error('Serveri viga');
        this.router.navigate(['/']);
      } else {
        this.router.navigate(['/']);
      }
    });
  }

  constructor(private systemsService: SystemsService,
              private environmentService: EnvironmentService,
              private route: ActivatedRoute,
              private router: Router,
              private toastrService: ToastrService,
              private winRef: WindowRefService) {
    this.user = this.environmentService.getActiveUser();
  }

  ngOnInit() {
    this.route.params.subscribe( params => {
      this.loaded = false;
      this.notFound = false;
      this.issueId = params['issue_id'] || null;
      this.getSystem(params['short_name']);
    });
  }

  @HostListener("window:scroll", [])
  onWindowScroll() {

  }
}
