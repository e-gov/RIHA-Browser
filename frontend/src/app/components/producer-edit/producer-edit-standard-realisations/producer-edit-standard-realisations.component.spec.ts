import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ProducerEditStandardRealisationsComponent} from './producer-edit-standard-realisations.component';

describe('ProducerEditStandardRealisationsComponent', () => {
  let component: ProducerEditStandardRealisationsComponent;
  let fixture: ComponentFixture<ProducerEditStandardRealisationsComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerEditStandardRealisationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerEditStandardRealisationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
