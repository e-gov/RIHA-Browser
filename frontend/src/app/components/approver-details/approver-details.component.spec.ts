import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproverDetailsComponent } from './approver-details.component';

describe('ApproverDetailsComponent', () => {
  let component: ApproverDetailsComponent;
  let fixture: ComponentFixture<ApproverDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApproverDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproverDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
