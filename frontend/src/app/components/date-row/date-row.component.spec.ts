import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DateRowComponent } from './date-row.component';

describe('DateRowComponent', () => {
  let component: DateRowComponent;
  let fixture: ComponentFixture<DateRowComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DateRowComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DateRowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
