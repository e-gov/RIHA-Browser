
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

  getSortProperty(): string {
    let ret = null;
    if (this.sort){
      ret = this.sort.charAt(0) == '-' ? this.sort.substring(1) : this.sort;
    }
    return ret;
  }

  getSortOrder(): string {
    let ret = null;
    if (this.sort){
      ret = this.sort.charAt(0) == '-' ? 'DESC' : 'ASC';
    }
    return ret;
  }

  updateData(data: any, contentTransformer?: any): void {
    if (data.totalElements) this.totalElements = data.totalElements;
    if (data.content) {
      if (contentTransformer) {
        this.content = contentTransformer(data.content);
      } else {
        this.content = data.content;
      }
    }
    if (data.size) this.size = data.size;
    if (data.totalPages) this.totalPages = data.totalPages;
    if (data.page) this.page = data.page;
    if (data.sort) this.sort = data.sort;
  }

  changeSortOrder(prop: string, dir?: string): void {
    if (dir && (dir == 'ASC' || dir == 'DESC')){
      if (dir == 'ASC'){
        this.sort = prop;
      } else if (dir == 'DESC'){
        this.sort = '-' + prop;
      }
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

  setPageFromUrl(page: string): void{
    if (page){
      const pageNumber = Number(page);
      this.page = !isNaN(pageNumber) ? pageNumber - 1 : 0;
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
