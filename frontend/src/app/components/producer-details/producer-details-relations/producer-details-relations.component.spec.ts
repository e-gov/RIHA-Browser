import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProducerDetailsRelationsComponent } from './producer-details-relations.component';

describe('ProducerDetailsRelationsComponent', () => {
  let component: ProducerDetailsRelationsComponent;
  let fixture: ComponentFixture<ProducerDetailsRelationsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerDetailsRelationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerDetailsRelationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
