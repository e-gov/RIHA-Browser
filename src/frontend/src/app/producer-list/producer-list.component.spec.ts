import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProducerListComponent } from './producer-list.component';

describe('ProducerListComponent', () => {
  let component: ProducerListComponent;
  let fixture: ComponentFixture<ProducerListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
