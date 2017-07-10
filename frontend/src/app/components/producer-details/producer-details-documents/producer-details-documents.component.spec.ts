import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProducerDetailsTechDocsComponent } from './producer-details-documents.component';

describe('ProducerDetailsTechDocsComponent', () => {
  let component: ProducerDetailsTechDocsComponent;
  let fixture: ComponentFixture<ProducerDetailsTechDocsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerDetailsTechDocsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerDetailsTechDocsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
