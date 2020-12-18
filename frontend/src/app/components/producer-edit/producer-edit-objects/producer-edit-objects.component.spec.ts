import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ProducerEditObjectsComponent} from './producer-edit-objects.component';

describe('ProducerEditObjectsComponent', () => {
  let component: ProducerEditObjectsComponent;
  let fixture: ComponentFixture<ProducerEditObjectsComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerEditObjectsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerEditObjectsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
