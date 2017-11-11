import { TestBed, inject } from '@angular/core/testing';

import { SessionHelperService } from './session-helper.service';

describe('SessionHelperService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SessionHelperService]
    });
  });

  it('should be created', inject([SessionHelperService], (service: SessionHelperService) => {
    expect(service).toBeTruthy();
  }));
});
