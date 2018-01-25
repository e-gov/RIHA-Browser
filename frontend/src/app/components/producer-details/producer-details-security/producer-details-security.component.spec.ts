import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProducerDetailsSecurityComponent } from './producer-details-security.component';

describe('ProducerDetailsSecurityComponent', () => {
  let component: ProducerDetailsSecurityComponent;
  let fixture: ComponentFixture<ProducerDetailsSecurityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerDetailsSecurityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerDetailsSecurityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
