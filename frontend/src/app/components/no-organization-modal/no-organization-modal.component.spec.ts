import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {NoOrganizationModalComponent} from './no-organization-modal.component';

describe('NoOrganizationModalComponent', () => {
  let component: NoOrganizationModalComponent;
  let fixture: ComponentFixture<NoOrganizationModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NoOrganizationModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NoOrganizationModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
