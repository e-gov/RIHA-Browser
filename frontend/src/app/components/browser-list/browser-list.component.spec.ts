import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {BrowserListComponent} from './browser-list.component';

describe('BrowserListComponent', () => {
  let component: BrowserListComponent;
  let fixture: ComponentFixture<BrowserListComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ BrowserListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BrowserListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
