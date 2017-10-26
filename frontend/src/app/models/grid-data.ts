import {isNullOrUndefined} from "util";

export class GridData {
  totalElements: number;
  content: any[];
  size: number;
  totalPages: number;
  page: number;
  sort: string;

  getSize(): number {
    return this.totalElements;
  }

  getPageNumber(): number {
    return this.page + 1;
  }

  updateData(data: any): void {
    if (!isNullOrUndefined(data.totalElements)) this.totalElements = data.totalElements;
    if (!isNullOrUndefined(data.content)) this.content = data.content;
    if (!isNullOrUndefined(data.size)) this.size = data.size;
    if (!isNullOrUndefined(data.totalPages)) this.totalPages = data.totalPages;
    if (!isNullOrUndefined(data.page)) this.page = data.page;
    if (!isNullOrUndefined(data.sort)) this.sort = data.sort;
  }

  changeSortOrder(prop: string, dir?: string): void {
    if (dir){
      this.sort = dir + prop;
    } else if (-1 != prop.indexOf(this.sort)){
      if (-1 != '-'.indexOf(this.sort)){
        this.sort = prop;
      } else {
        this.sort = '-' + this.sort;
      }
    } else {
      this.sort = prop;
    }
  }

  constructor(){
    this.totalElements = 0;
    this.content = [];
    this.size = 0;
    this.totalPages = 0;
    this.page = 0;
    this.sort = null;
  }
}
