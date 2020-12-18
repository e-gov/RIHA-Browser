import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ProducerEditComponent} from './producer-edit.component';

describe('ProducerEditComponent', () => {
  let component: ProducerEditComponent;
  let fixture: ComponentFixture<ProducerEditComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
