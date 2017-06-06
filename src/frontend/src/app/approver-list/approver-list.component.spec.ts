import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproverListComponent } from './approver-list.component';

describe('ApproverListComponent', () => {
  let component: ApproverListComponent;
  let fixture: ComponentFixture<ApproverListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApproverListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproverListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
