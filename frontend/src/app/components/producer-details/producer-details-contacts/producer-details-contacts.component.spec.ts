import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProducerDetailsContactsComponent } from './producer-details-contacts.component';

describe('ProducerDetailsContactsComponent', () => {
  let component: ProducerDetailsContactsComponent;
  let fixture: ComponentFixture<ProducerDetailsContactsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerDetailsContactsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerDetailsContactsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
