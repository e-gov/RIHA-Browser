import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ProducerEditContactsComponent} from './producer-edit-contacts.component';

describe('ProducerEditContactsComponent', () => {
  let component: ProducerEditContactsComponent;
  let fixture: ComponentFixture<ProducerEditContactsComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerEditContactsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerEditContactsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
