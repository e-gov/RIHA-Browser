import { Component, OnInit, HostListener } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { EnvironmentService } from "../../services/environment.service";
import { ActivatedRoute } from '@angular/router';
import { System } from '../../models/system';
import { User } from '../../models/user';
import { WindowRefService } from '../../services/window-ref.service';

declare var $: any;

@Component({
  selector: 'app-producer-details',
  templateUrl: './producer-details.component.html',
  styleUrls: ['./producer-details.component.scss']
})
export class ProducerDetailsComponent implements OnInit {
  private system: System = new System();
  private user: User;
  private loaded: boolean = false;

  adjustSection(attempt){
    if (attempt < 5){
      let hash = this.winRef.nativeWindow.location.hash;
      if (hash){
        let elId = decodeURI(hash.replace('#',''));
        let el = $(hash)[0];
        if (el){
          this.winRef.nativeWindow.scrollTo(0,$(el).offset().top);
        } else {
          attempt++;
          setTimeout(()=>{this.adjustSection(attempt)}, 1000);
        }
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

  getSystem(id){
    this.systemsService.getSystem(id).then(response => {
      this.system = new System(response.json());
      this.loaded = true;
      this.adjustSection(0);
    })
  }

  constructor(private systemsService: SystemsService,
              private environmentService: EnvironmentService,
              private route: ActivatedRoute,
              private winRef: WindowRefService) {
    this.user = this.environmentService.getActiveUser();
  }

  ngOnInit() {
    this.route.params.subscribe( params => {
      this.loaded = false;
      this.getSystem(params['short_name']);
    });
  }

  @HostListener("window:scroll", [])
  onWindowScroll() {

  }
}
