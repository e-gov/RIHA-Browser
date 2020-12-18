import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {GridTotalFoundComponent} from './grid-total-found.component';

describe('GridTotalFoundComponent', () => {
  let component: GridTotalFoundComponent;
  let fixture: ComponentFixture<GridTotalFoundComponent>;

  beforeEach(waitForAsync(() => {
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
