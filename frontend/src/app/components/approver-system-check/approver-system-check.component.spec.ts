import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproverSystemCheckComponent } from './approver-system-check.component';

describe('ApproverSystemCheckComponent', () => {
  let component: ApproverSystemCheckComponent;
  let fixture: ComponentFixture<ApproverSystemCheckComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApproverSystemCheckComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproverSystemCheckComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
