import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { SystemsService } from '../../../services/systems.service';
import { System } from '../../../models/system';
import { ToastrService } from 'ngx-toastr';
import { ModalHelperService } from '../../../services/modal-helper.service';
import { GeneralHelperService } from '../../../services/general-helper.service';
import { Observable } from 'rxjs';
import { NgForm } from '@angular/forms';
import { CanDeactivateModal } from '../../../guards/can-deactivate-modal.guard';
import { CONSTANTS } from '../../../utils/constants';

@Component({
  selector: 'app-producer-edit-contacts',
  templateUrl: './producer-edit-contacts.component.html',
  styleUrls: ['./producer-edit-contacts.component.scss'],
  standalone: false,
})
export class ProducerEditContactsComponent implements OnInit, CanDeactivateModal {
  @ViewChild('addForm') formObject: NgForm;

  @Input() system: System;
  contacts: any[] = [];
  isChanged: boolean = false;
  buttonClicked: boolean = false;

  data: any = { email: '', name: '' };

  addContact(addForm): void {
    if (addForm.valid) {
      this.contacts.push({ email: this.data.email, name: this.data.name ? this.data.name.trim() : '' });
      this.data = { email: '', name: '' };
      addForm.reset();
      this.isChanged = true;
      this.buttonClicked = false;
    } else {
      this.buttonClicked = true;
    }
  }

  deleteContact(i): void {
    this.contacts.splice(i, 1);
    this.isChanged = true;
  }

  saveSystem() {
    this.systemsService.getSystem(this.system.details.short_name).subscribe(
      responseSystem => {
        const system = new System(responseSystem);
        system.details.contacts = this.contacts;
        this.systemsService.updateSystem(system).subscribe(
          updatedSystem => {
            this.modalService.closeActiveModal({ system: new System(updatedSystem) });
          },
          err => {
            this.handleValidationError(err);
          },
        );
      },
      err => {
        this.toastrService.error('Serveri viga.');
      },
    );
  }

  private handleValidationError(err: any): void {
    // Check if this is a JSON validation error with email format issue
    if (err.error && Array.isArray(err.error)) {
      for (const validationError of err.error) {
        // Check for the specific error pattern: format validation with "not-a-timestamp" value
        // This indicates an email validation error despite the misleading error message
        if (validationError.keyword === 'format' && 
            validationError.value === 'not-a-timestamp') {
          this.toastrService.error('Viga e-posti aadressi valideerimisel');
          return;
        }
        // Also check for other email-related format errors
        if (validationError.keyword === 'format' && 
            (validationError.schemaPath?.includes('email') || 
             validationError.instancePath?.includes('email') ||
             validationError.instancePath?.includes('contacts'))) {
          this.toastrService.error('Viga e-posti aadressi valideerimisel');
          return;
        }
      }
    }
    
    // Default error message for other types of errors
    this.toastrService.error('Serveri viga.');
  }

  canDeactivate(): Observable<boolean> | Promise<boolean> | boolean {
    return this.closeModal();
  }

  closeModal() {
    if (this.isFormChanged) {
      const observer = this.modalService.confirm(CONSTANTS.CLOSE_DIALOG_WARNING);
      observer.subscribe(confirmed => {
        if (confirmed) {
          this.modalService.dismissActiveModal();
        }
      });
      return observer;
    }

    this.modalService.dismissActiveModal();
    return true;
  }

  /**
   * Getters
   */

  /**
   * Is form data changed ?
   */
  get isFormChanged(): boolean {
    return this.isChanged || this.formObject.form.dirty;
  }

  constructor(
    private modalService: ModalHelperService,
    private systemsService: SystemsService,
    private toastrService: ToastrService,
    private generalHelperService: GeneralHelperService,
  ) {}

  ngOnInit() {
    const system = this.generalHelperService.cloneObject(this.system);
    this.contacts = system.details.contacts || [];
  }
}
