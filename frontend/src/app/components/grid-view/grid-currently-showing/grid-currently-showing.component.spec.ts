import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GridCurrentlyShowingComponent } from './grid-currently-showing.component';

describe('GridCurrentlyShowingComponent', () => {
  let component: GridCurrentlyShowingComponent;
  let fixture: ComponentFixture<GridCurrentlyShowingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GridCurrentlyShowingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GridCurrentlyShowingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
