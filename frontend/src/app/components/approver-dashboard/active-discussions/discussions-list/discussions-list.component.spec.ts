import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {DiscussionsListComponent} from './discussions-list.component';

describe('DiscussionsListComponent', () => {
  let component: DiscussionsListComponent;
  let fixture: ComponentFixture<DiscussionsListComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ DiscussionsListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiscussionsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
