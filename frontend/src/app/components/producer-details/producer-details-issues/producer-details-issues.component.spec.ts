import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProducerDetailsIssuesComponent } from './producer-details-issues.component';

describe('ProducerDetailsIssuesComponent', () => {
  let component: ProducerDetailsIssuesComponent;
  let fixture: ComponentFixture<ProducerDetailsIssuesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerDetailsIssuesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerDetailsIssuesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
