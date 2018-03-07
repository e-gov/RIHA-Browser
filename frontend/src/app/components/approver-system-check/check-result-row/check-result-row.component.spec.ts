import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckResultRowComponent } from './check-result-row.component';

describe('CheckResultRowComponent', () => {
  let component: CheckResultRowComponent;
  let fixture: ComponentFixture<CheckResultRowComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CheckResultRowComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CheckResultRowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
