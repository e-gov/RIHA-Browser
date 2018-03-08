import { Component, OnInit, DoCheck, KeyValueDiffers } from '@angular/core';
import { UserMatrix } from '../../models/user-matrix';
import { EnvironmentService } from '../../services/environment.service';
import { GridData } from '../../models/grid-data';

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

  private getOwnOpenIssues(){
    this.loaded = true;
    if (this.userMatrix.isLoggedIn && this.userMatrix.isOrganizationSelected) {

    }
  }

  constructor(private differs: KeyValueDiffers,
              private environmentService: EnvironmentService) {
    this.differ = differs.find({}).create(null);
    this.userMatrix = this.environmentService.getUserMatrix();
  }

  ngOnInit() {
    this.getOwnOpenIssues();
  }

  ngDoCheck() {
    let changes = this.differ.diff(this.environmentService.globalEnvironment);
    if (changes && (this.loaded || !this.userMatrix.isOrganizationSelected)){
      this.userMatrix = this.environmentService.getUserMatrix();
      this.getOwnOpenIssues();
    }
  }

}
