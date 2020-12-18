import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ProducerDetailsObjectsComponent} from './producer-details-objects.component';

describe('ProducerDetailsObjectsComponent', () => {
  let component: ProducerDetailsObjectsComponent;
  let fixture: ComponentFixture<ProducerDetailsObjectsComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerDetailsObjectsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerDetailsObjectsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
