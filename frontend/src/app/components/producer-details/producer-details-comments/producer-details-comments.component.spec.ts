import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProducerDetailsCommentsComponent } from './producer-details-comments.component';

describe('ProducerDetailsCommentsComponent', () => {
  let component: ProducerDetailsCommentsComponent;
  let fixture: ComponentFixture<ProducerDetailsCommentsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProducerDetailsCommentsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProducerDetailsCommentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
