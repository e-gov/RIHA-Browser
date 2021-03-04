import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ProducerEditGeneralComponent} from './producer-edit-general.component';

describe('ProducerEditGeneralComponent', () => {
  let component: ProducerEditGeneralComponent;
  let fixture: ComponentFixture<ProducerEditGeneralComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerEditGeneralComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerEditGeneralComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
