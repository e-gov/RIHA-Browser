import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FileHintComponent } from './file-hint.component';

describe('FileHintComponent', () => {
  let component: FileHintComponent;
  let fixture: ComponentFixture<FileHintComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FileHintComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FileHintComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
