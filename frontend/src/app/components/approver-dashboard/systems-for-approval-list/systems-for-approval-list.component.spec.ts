import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SystemsForApprovalListComponent } from './systems-for-approval-list.component';

describe('SystemsForApprovalListComponent', () => {
  let component: SystemsForApprovalListComponent;
  let fixture: ComponentFixture<SystemsForApprovalListComponent>;

  beforeEach(async(() => {
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
