export class GridData {
  totalElements: number;
  content: any[];
  size: number;
  totalPages: number;
  page: number;

  updateData(data: any): void {
    if (data.totalElements) this.totalElements = data.totalElements;
    if (data.content) this.content = data.content;
    if (data.size) this.size = data.size;
    if (data.totalPages) this.totalPages = data.totalPages;
    if (data.page) this.page = data.page;
  }

  constructor(){
    this.totalElements = 0;
    this.content = [];
    this.size = 0;
    this.totalPages = 0;
    this.page = 0;
  }
}
