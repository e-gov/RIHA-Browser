import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ProducerSearchFilterComponent} from './producer-search-filter-component';

describe('ProducerListComponent', () => {
  let component: ProducerSearchFilterComponent;
  let fixture: ComponentFixture<ProducerSearchFilterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerSearchFilterComponent ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerSearchFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
