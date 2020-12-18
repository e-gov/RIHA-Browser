import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ProducerAddComponent} from './producer-add.component';

describe('ProducerAddComponent', () => {
  let component: ProducerAddComponent;
  let fixture: ComponentFixture<ProducerAddComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerAddComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
