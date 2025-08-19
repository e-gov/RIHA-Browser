import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-file-icon',
  templateUrl: './file-icon.component.html',
  styleUrls: ['./file-icon.component.scss'],
  standalone: false,
})
export class FileIconComponent implements OnInit {
  @Input() file: any;

  isUploaded() {
    return this.file.url.substr(0, 7) == 'file://';
  }

  constructor() {}

  ngOnInit() {}
}
