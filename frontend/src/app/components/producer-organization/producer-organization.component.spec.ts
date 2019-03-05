import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProducerOrganizationComponent } from './producer-organization.component';

describe('ProducerOrganizationComponent', () => {
  let component: ProducerOrganizationComponent;
  let fixture: ComponentFixture<ProducerOrganizationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerOrganizationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerOrganizationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
