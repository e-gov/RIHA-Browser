import { Component, OnInit, Input } from '@angular/core';
import { G } from '../../../globals/globals';

@Component({
  selector: 'app-file-hint',
  templateUrl: './file-hint.component.html',
  styleUrls: ['./file-hint.component.scss']
})
export class FileHintComponent implements OnInit {

  @Input() file: any;

  constructor() {
  }

  ngOnInit() {
  }

}
