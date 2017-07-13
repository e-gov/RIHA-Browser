import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProducerDetailsLegislationsComponent } from './producer-details-legislations.component';

describe('ProducerDetailsLegislationsComponent', () => {
  let component: ProducerDetailsLegislationsComponent;
  let fixture: ComponentFixture<ProducerDetailsLegislationsComponent>;

  beforeEach(async(() => {
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
