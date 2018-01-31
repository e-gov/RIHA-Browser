import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SortButtonComponent } from './sort-button.component';

describe('SortButtonComponent', () => {
  let component: SortButtonComponent;
  let fixture: ComponentFixture<SortButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SortButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SortButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
