import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproverDashboardComponent } from './approver-dashboard.component';

describe('ApproverDashboardComponent', () => {
  let component: ApproverDashboardComponent;
  let fixture: ComponentFixture<ApproverDashboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApproverDashboardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproverDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
