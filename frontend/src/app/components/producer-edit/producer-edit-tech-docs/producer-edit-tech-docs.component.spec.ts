import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProducerEditTechDocsComponent } from './producer-edit-tech-docs.component';

describe('ProducerEditTechDocsComponent', () => {
  let component: ProducerEditTechDocsComponent;
  let fixture: ComponentFixture<ProducerEditTechDocsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerEditTechDocsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerEditTechDocsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
