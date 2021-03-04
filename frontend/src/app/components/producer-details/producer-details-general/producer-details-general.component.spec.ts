import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ProducerDetailsGeneralComponent} from './producer-details-general.component';

describe('ProducerDetailsGeneralComponent', () => {
  let component: ProducerDetailsGeneralComponent;
  let fixture: ComponentFixture<ProducerDetailsGeneralComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerDetailsGeneralComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerDetailsGeneralComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
