import { Component, OnInit, Input } from '@angular/core';
import { SystemsService } from '../../../../services/systems.service';
import { ToastrService } from 'ngx-toastr';
import { GridData } from '../../../../models/grid-data';
import { EnvironmentService } from '../../../../services/environment.service';
import { GeneralHelperService } from '../../../../services/general-helper.service';
import { G } from '../../../../globals/globals';
import * as moment from 'moment';

@Component({
  selector: 'app-discussions-list',
  templateUrl: './discussions-list.component.html',
  styleUrls: ['./discussions-list.component.scss']
})
export class DiscussionsListComponent implements OnInit {

  @Input() relation: string;
  public loaded: boolean = false;
  public gridData: GridData = new GridData();
  private globals: any = G;

  public onSortChange(property): void{
    this.gridData.changeSortOrder(property);
    this.getActiveDiscussions();
  }

  private getActiveDiscussions(){
    this.systemsService.getActiveDiscussions(this.gridData.sort, this.relation).then( res => {
      this.gridData.updateData(res.json());
      this.loaded = true;
    }, err => {
      this.loaded = true;
      this.toastrService.error('Serveri viga!');
    });
  }

  constructor(private systemsService: SystemsService,
              private helper: GeneralHelperService,
              private environmentService: EnvironmentService,
              private toastrService: ToastrService) { }

  ngOnInit() {
    this.gridData.changeSortOrder('creation_date', 'ASC');
    this.getActiveDiscussions();
  }

}
