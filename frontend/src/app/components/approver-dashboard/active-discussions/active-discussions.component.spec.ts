import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiveDiscussionsComponent } from './active-discussions.component';

describe('ActiveDiscussionsComponent', () => {
  let component: ActiveDiscussionsComponent;
  let fixture: ComponentFixture<ActiveDiscussionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ActiveDiscussionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActiveDiscussionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
