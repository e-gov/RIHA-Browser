import { TestBed, inject } from '@angular/core/testing';

import { ModalHelperService } from './modal-helper.service';

describe('ModalHelperService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ModalHelperService]
    });
  });

  it('should be created', inject([ModalHelperService], (service: ModalHelperService) => {
    expect(service).toBeTruthy();
  }));
});
