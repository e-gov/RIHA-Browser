import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproverAddCommentComponent } from './approver-add-comment.component';

describe('ApproverAddCommentComponent', () => {
  let component: ApproverAddCommentComponent;
  let fixture: ComponentFixture<ApproverAddCommentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApproverAddCommentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproverAddCommentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
