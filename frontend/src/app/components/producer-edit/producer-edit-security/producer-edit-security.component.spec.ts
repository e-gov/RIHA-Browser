import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ProducerEditSecurityComponent} from './producer-edit-security.component';

describe('ProducerEditSecurityComponent', () => {
  let component: ProducerEditSecurityComponent;
  let fixture: ComponentFixture<ProducerEditSecurityComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerEditSecurityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerEditSecurityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
