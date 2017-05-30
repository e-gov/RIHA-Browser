import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardDeckComponent } from './card-deck.component';

describe('CardDeckComponent', () => {
  let component: CardDeckComponent;
  let fixture: ComponentFixture<CardDeckComponent>;
  let resetTestBed;

  beforeAll(() => {
    resetTestBed = TestBed.resetTestingModule;
    TestBed.resetTestingModule = () => {
      return TestBed;
    };
  });

  afterAll(() => {
    resetTestBed();
    TestBed.resetTestingModule = resetTestBed;
  });

  beforeAll((done) => new Promise((resolve) => {
    TestBed.configureTestingModule({
        declarations: [CardDeckComponent]
      })
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(CardDeckComponent);
        component = fixture.componentInstance;
        fixture.autoDetectChanges(true);
        resolve();
      });
  }).then(done));

  it('creates CardDeckComponent', () => {
    expect(component).toBeTruthy();
  });

  it('shows three cards', () => {
    expect(fixture.nativeElement.querySelectorAll('.card').length).toEqual(3);
  });
});
