import { Injectable } from '@angular/core';
import { NgbModal, NgbModalOptions, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";

@Injectable()
export class ModalHelperService {

  private lastModalRef: NgbModalRef = null;

  public dismissActiveModal(): void{
    if (this.lastModalRef){
      this.lastModalRef.dismiss();
    }
  }

  public open(content: any, options?: NgbModalOptions){
    return this.lastModalRef = this.modalService.open(content, options);
  }

  constructor(private modalService: NgbModal) { }

}
