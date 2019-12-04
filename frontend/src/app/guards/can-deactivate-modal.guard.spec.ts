import {inject, TestBed} from '@angular/core/testing';

import {CanDeactivateModalGuard} from './can-deactivate-modal.guard';

describe('CanDeactivateModalGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CanDeactivateModalGuard]
    });
  });

  it('should ...', inject([CanDeactivateModalGuard], (guard: CanDeactivateModalGuard) => {
    expect(guard).toBeTruthy();
  }));
});
