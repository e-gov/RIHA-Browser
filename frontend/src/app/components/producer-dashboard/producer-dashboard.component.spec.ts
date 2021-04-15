import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ProducerDashboardComponent} from './producer-dashboard.component';

describe('ProducerDashboardComponent', () => {
  let component: ProducerDashboardComponent;
  let fixture: ComponentFixture<ProducerDashboardComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerDashboardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
