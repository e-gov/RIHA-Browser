import { Pipe, PipeTransform } from '@angular/core';
import linkifyStr from 'linkify-string';

@Pipe({
    name: 'linkify',
    standalone: false
})
export class LinkifyPipe implements PipeTransform {
  transform(str: string): string {
    return str ? linkifyStr(str, {
      className: 'user-link',
    }) : str;
  }
}
