import { Component, OnInit } from '@angular/core';
import { GeneralHelperService } from '../../services/general-helper.service';

@Component({
    selector: 'app-page-not-found',
    templateUrl: './page-not-found.component.html',
    styleUrls: ['./page-not-found.component.scss'],
    standalone: false
})
export class PageNotFoundComponent implements OnInit {

  constructor(private generalHelperService: GeneralHelperService) { }

  ngOnInit() {
    this.generalHelperService.setRihaPageTitle('Lehekülge ei leitud');
  }

}
