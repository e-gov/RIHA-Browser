import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopicsButtonComponent } from './topics-button.component';

describe('TopicsButtonComponent', () => {
  let component: TopicsButtonComponent;
  let fixture: ComponentFixture<TopicsButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TopicsButtonComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TopicsButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
