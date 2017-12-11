import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProducerEditRelationsComponent } from './producer-edit-relations.component';

describe('ProducerEditRelationsComponent', () => {
  let component: ProducerEditRelationsComponent;
  let fixture: ComponentFixture<ProducerEditRelationsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerEditRelationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerEditRelationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
