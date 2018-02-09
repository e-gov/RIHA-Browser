import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment';

@Pipe({
  name: 'datem'
})
export class DatemPipe implements PipeTransform {
  transform(value: any, format: string = ""): string {

    let momentDate = moment(value);

    if (!momentDate.isValid()) return value;

    return momentDate.format(format);
  }
}
