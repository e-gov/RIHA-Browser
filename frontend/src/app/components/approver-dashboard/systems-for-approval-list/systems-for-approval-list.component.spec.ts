import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {SystemsForApprovalListComponent} from './systems-for-approval-list.component';

describe('SystemsForApprovalListComponent', () => {
  let component: SystemsForApprovalListComponent;
  let fixture: ComponentFixture<SystemsForApprovalListComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ SystemsForApprovalListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SystemsForApprovalListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
