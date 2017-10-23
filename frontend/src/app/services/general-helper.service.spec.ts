import { TestBed, inject } from '@angular/core/testing';

import { GeneralHelperServiceService } from './general-helper.service';

describe('GeneralHelperServiceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GeneralHelperServiceService]
    });
  });

  it('should be created', inject([GeneralHelperServiceService], (service: GeneralHelperServiceService) => {
    expect(service).toBeTruthy();
  }));
});
