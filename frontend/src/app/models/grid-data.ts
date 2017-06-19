export class GridData {
  totalElements: number;
  content: any[];
  size: number;
  totalPages: number;
  page: number;
  sort: string;

  updateData(data: any): void {
    if (data.totalElements) this.totalElements = data.totalElements;
    if (data.content) this.content = data.content;
    if (data.size) this.size = data.size;
    if (data.totalPages) this.totalPages = data.totalPages;
    if (data.page) this.page = data.page;
    if (data.sort) this.sort = data.sort;
  }

  changeSortOrder(prop: string): void {
    if (-1 != prop.indexOf(this.sort)){
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
