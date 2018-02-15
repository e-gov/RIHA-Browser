import { Pipe, PipeTransform } from '@angular/core';
import linkify from 'linkifyjs/string';

@Pipe({name: 'linkify'})
export class LinkifyPipe implements PipeTransform {
  transform(str: string): string {
    return str ? linkify(str, {
      className: 'user-link',
    }) : str;
  }
}
