import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-file-create-update-date',
  templateUrl: './file-create-update-date.component.html',
  styleUrls: ['./file-create-update-date.component.scss']
})
export class FileCreateUpdateDateComponent implements OnInit {

  @Input() file: any;

  isUploaded(){
    return this.file.url.substr(0,7) == 'file://';
  }

  constructor() { }

  ngOnInit() {
  }

}
