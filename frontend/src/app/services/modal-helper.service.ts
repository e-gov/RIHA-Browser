import { Injectable } from '@angular/core';
import { NgbModal, NgbModalOptions, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";

@Injectable()
export class ModalHelperService {

  private lastModalRef: NgbModalRef = null;

  private modalRefs: NgbModalRef[] = [];

  public dismissActiveModal(reason?): void{
    if (this.modalRefs.length > 0){
      let modalRef = this.modalRefs.pop();
      modalRef.dismiss(reason);
    }
  }

  public closeActiveModal(result?): void{
    if (this.modalRefs.length > 0){
      let modalRef = this.modalRefs.pop();
      modalRef.close(result);
    }
  }

  public dismissAllModals(){
    while (this.modalRefs.length > 0){
      this.dismissActiveModal();
    }
  }

  public open(content: any, options?: NgbModalOptions, keepStacked?: boolean): NgbModalRef{
    if (keepStacked !== true){
      this.dismissAllModals();
    }
    let modalRef = this.modalService.open(content, options);
    this.modalRefs.push(modalRef);
    return modalRef;
  }

  constructor(private modalService: NgbModal) { }

}
