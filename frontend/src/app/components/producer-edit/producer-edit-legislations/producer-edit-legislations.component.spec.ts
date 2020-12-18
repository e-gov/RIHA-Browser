import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ProducerEditLegislationsComponent} from './producer-edit-legislations.component';

describe('ProducerEditLegislationsComponent', () => {
  let component: ProducerEditLegislationsComponent;
  let fixture: ComponentFixture<ProducerEditLegislationsComponent>;

  beforeEach(waitForAsync(() => {
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
