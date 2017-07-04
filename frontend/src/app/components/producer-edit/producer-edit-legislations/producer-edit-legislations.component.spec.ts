import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProducerEditLegislationsComponent } from './producer-edit-legislations.component';

describe('ProducerEditLegislationsComponent', () => {
  let component: ProducerEditLegislationsComponent;
  let fixture: ComponentFixture<ProducerEditLegislationsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerEditLegislationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerEditLegislationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
