import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GridTotalFoundComponent } from './grid-total-found.component';

describe('GridTotalFoundComponent', () => {
  let component: GridTotalFoundComponent;
  let fixture: ComponentFixture<GridTotalFoundComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GridTotalFoundComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GridTotalFoundComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
