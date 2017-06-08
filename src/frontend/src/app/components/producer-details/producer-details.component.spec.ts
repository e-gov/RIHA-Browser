import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProducerDetailsComponent } from './producer-details.component';

describe('ProducerDetailsComponent', () => {
  let component: ProducerDetailsComponent;
  let fixture: ComponentFixture<ProducerDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
