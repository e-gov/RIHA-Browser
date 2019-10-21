import { Component, OnInit, Input, DoCheck, KeyValueDiffers  } from '@angular/core';
import { SystemsService } from '../../../../services/systems.service';
import { ToastrService } from 'ngx-toastr';
import { GridData } from '../../../../models/grid-data';
import { EnvironmentService } from '../../../../services/environment.service';
import { GeneralHelperService } from '../../../../services/general-helper.service';
import { classifiers } from "../../../../services/environment.service";
import { UserMatrix } from '../../../../models/user-matrix';

@Component({
  selector: 'app-discussions-list',
  templateUrl: './discussions-list.component.html',
  styleUrls: ['./discussions-list.component.scss']
})
export class DiscussionsListComponent implements OnInit, DoCheck {

  @Input() relation: string;
  classifiers = classifiers;
  public loaded: boolean = false;
  public gridData: GridData = new GridData();
  private differ: any;
  private userMatrix: UserMatrix;

  public isNewDiscussion(ad){
    if (ad.lastComment){
      return ad.lastComment.organizationCode != this.environmentService.getActiveUser().activeOrganization.code;
    } else {
      return true;
    }
  }

  private getActiveDiscussions(){
    this.systemsService.getActiveDiscussions(this.gridData.sort, this.relation).subscribe( res => {
      this.gridData.updateData(res);
      this.loaded = true;
    }, err => {
      this.loaded = true;
      this.toastrService.error('Serveri viga!');
    });
  }

  constructor(private systemsService: SystemsService,
              private helper: GeneralHelperService,
              private differs: KeyValueDiffers,
              private environmentService: EnvironmentService,
              private toastrService: ToastrService) {
    this.differ = differs.find({}).create();
  }

  ngOnInit() {
    this.gridData.changeSortOrder('last_comment_creation_date', 'DESC');
    this.getActiveDiscussions();
  }

  ngDoCheck() {
    var changes = this.differ.diff(this.environmentService.globalEnvironment);
    if (changes && (this.loaded || !this.environmentService.getUserMatrix().isOrganizationSelected)){
      this.loaded = false;
      this.getActiveDiscussions();
    }
  }
}
