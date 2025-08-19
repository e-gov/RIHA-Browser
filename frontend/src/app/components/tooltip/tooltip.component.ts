import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-tooltip',
  templateUrl: './tooltip.component.html',
  styleUrls: ['./tooltip.component.scss'],
  standalone: false,
})
export class TooltipComponent implements OnInit {
  @Input() text: string;
  @Input() placement?: string;

  constructor() {}

  ngOnInit() {}
}
