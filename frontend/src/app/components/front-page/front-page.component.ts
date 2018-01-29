import { Component, OnInit } from '@angular/core';
import { GeneralHelperService } from '../../services/general-helper.service';

@Component({
  selector: 'app-front-page',
  templateUrl: './front-page.component.html',
  styleUrls: ['./front-page.component.scss']
})
export class FrontPageComponent implements OnInit {

  constructor(private generalHelperService: GeneralHelperService) { }

  ngOnInit() {
    this.generalHelperService.setRihaPageTitle('Avaleht');
  }

}
