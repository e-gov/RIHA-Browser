import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ProducerEditDocumentsComponent} from './producer-edit-documents.component';

describe('ProducerEditDocumentsComponent', () => {
  let component: ProducerEditDocumentsComponent;
  let fixture: ComponentFixture<ProducerEditDocumentsComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerEditDocumentsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerEditDocumentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
