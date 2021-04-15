import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ApproverIssueDetailsComponent} from './approver-issue-details.component';

describe('ApproverIssueDetailsComponent', () => {
  let component: ApproverIssueDetailsComponent;
  let fixture: ComponentFixture<ApproverIssueDetailsComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ApproverIssueDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproverIssueDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
