import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ApproverAddIssueComponent} from './approver-add-issue.component';

describe('ApproverAddIssueComponent', () => {
  let component: ApproverAddIssueComponent;
  let fixture: ComponentFixture<ApproverAddIssueComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ApproverAddIssueComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproverAddIssueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
