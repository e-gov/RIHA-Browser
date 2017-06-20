import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProducerAddComponent } from './producer-add.component';

describe('ProducerAddComponent', () => {
  let component: ProducerAddComponent;
  let fixture: ComponentFixture<ProducerAddComponent>;

  beforeEach(async(() => {
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
