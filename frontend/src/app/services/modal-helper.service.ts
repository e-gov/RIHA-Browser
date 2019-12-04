import {Injectable} from '@angular/core';
import {NgbModal, NgbModalOptions, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {Observable, of} from 'rxjs';

@Injectable()
export class ModalHelperService {

  private lastModalRef: NgbModalRef = null;

  private modalRefs: NgbModalRef[] = [];

  public dismissActiveModal(reason?): void{
    if (this.modalRefs.length > 0){
      const modalRef = this.modalRefs.pop();
      modalRef.dismiss(reason);
    }
  }

  public closeActiveModal(result?): void{
    if (this.modalRefs.length > 0){
      const modalRef = this.modalRefs.pop();
      modalRef.close(result);
    }
  }

  public dismissAllModals(){
    while (this.modalRefs.length > 0){
      this.dismissActiveModal();
    }
  }

  public open(content: any, options?: NgbModalOptions, keepStacked?: boolean): NgbModalRef {
    if (keepStacked !== true){
      this.dismissAllModals();
    }
    const modalRef = this.modalService.open(content, options);
    this.modalRefs.push(modalRef);
    return modalRef;
  }
  /**
   * Ask user to confirm an action. `message` explains the action and choices.
   * Returns observable resolving to `true`=confirm or `false`=cancel
   */
  confirm(message?: string): Observable<boolean> {
    const confirmation = window.confirm(message || 'Is it OK?');

    return of(confirmation);
  };

  get hasActiveModal() { return this.modalRefs.length > 0; }

  get lastModal(): NgbModalRef  {
    if (this.hasActiveModal) {
      return this.modalRefs[this.modalRefs.length - 1];
    } else {
      return null;
    }
  }

  constructor(private modalService: NgbModal) { }

}
