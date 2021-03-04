import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ProducerDetailsLegislationsComponent} from './producer-details-legislations.component';

describe('ProducerDetailsLegislationsComponent', () => {
  let component: ProducerDetailsLegislationsComponent;
  let fixture: ComponentFixture<ProducerDetailsLegislationsComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerDetailsLegislationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerDetailsLegislationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
