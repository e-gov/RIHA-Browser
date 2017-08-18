import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproverFeedbackDetailsComponent } from './approver-feedback-details.component';

describe('ApproverFeedbackDetailsComponent', () => {
  let component: ApproverFeedbackDetailsComponent;
  let fixture: ComponentFixture<ApproverFeedbackDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApproverFeedbackDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproverFeedbackDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
