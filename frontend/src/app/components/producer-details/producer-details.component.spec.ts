import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ProducerDetailsComponent} from './producer-details.component';

describe('ProducerDetailsComponent', () => {
  let component: ProducerDetailsComponent;
  let fixture: ComponentFixture<ProducerDetailsComponent>;

  beforeEach(waitForAsync(() => {
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
