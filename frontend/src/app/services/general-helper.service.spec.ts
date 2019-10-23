import { TestBed, inject } from '@angular/core/testing';

import { GeneralHelperService } from './general-helper.service';

describe('GeneralHelperService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GeneralHelperService]
    });
  });

  it('should be created', inject([GeneralHelperService], (service: GeneralHelperService) => {
    expect(service).toBeTruthy();
  }));
});
