import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {BrowserFilesListComponent} from './browser-files-list.component';

describe('BrowserFilesListComponent', () => {
  let component: BrowserFilesListComponent;
  let fixture: ComponentFixture<BrowserFilesListComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ BrowserFilesListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BrowserFilesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
