import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Subject } from 'rxjs/Subject';
import 'rxjs/add/operator/startWith';

import { CardDeckComponent } from './card-deck.component';
import { JsonDataService } from '../json-data.service';

describe('CardDeckComponent', () => {
  let component: CardDeckComponent;
  let fixture: ComponentFixture<CardDeckComponent>;
  let resetTestBed;
  class ThreeCards {
    public cards = new Subject().startWith(this.getCards());

    getCards() {
      return ['first', 'second', 'third'].map(this.getCard);
    }

    getCard(name) {
      const itemsByName = {
        first: ['first-list-item1', 'first-list-item2'],
        second: [],
        third: ['third-list-item1', 'third-list-item2', 'third-list-item3'],
      };
      const linksByName = {
        first: [{label: 'first-link-1', hash: '#first-link-1'}],
        third: [{label: 'third-link-1', hash: '#third-link-1'}, {label: 'third-link-2', hash: '#third-link-2'}]
      };
      return {
        iconType: name,
        title: [name, 'title'].join('-'),
        content: [
          [name, 'paragraph'].join('-'),
          [[name, 'list', 'title'].join('-')].concat(itemsByName[name])
        ],
        actions: Object.assign({}, linksByName[name] ? {
          links: linksByName[name],
          buttons: linksByName[name]
        } : {})
      };
    }
  }

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
        declarations: [CardDeckComponent],
        providers: [
          {provide: JsonDataService, useClass: ThreeCards}
        ]
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

  it('shows three card icons', () => {
    expect(fixture.nativeElement.querySelectorAll('.card .card-title .fa').length).toEqual(3);
  });

  it('shows card icons with certain types', () => {
    const list = [].slice.call(fixture.nativeElement.querySelectorAll('.card .card-title .fa'));
    expect(list.map((el) => {
      return [].slice.call(el.classList).filter((className) => /^fa-/.test(className))[0];
    })).toEqual(['fa-first', 'fa-second', 'fa-third']);
  });

  it('shows card titles', () => {
    const list = [].slice.call(fixture.nativeElement.querySelectorAll('.card .card-title'));
    expect(list.map((el) => el.textContent.trim())).toEqual(['first-title', 'second-title', 'third-title']);
  });

  it('shows first card paragraphs', () => {
    const list = [].slice.call(fixture.nativeElement.querySelectorAll('.card .card-text p:first-child'));
    expect(list.map((el) => el.textContent.trim())).toEqual(['first-paragraph', 'second-paragraph', 'third-paragraph']);
  });

  it('shows card list titles in content', () => {
    const list = [].slice.call(fixture.nativeElement.querySelectorAll('.card .card-text p:first-child + p strong:first-child'));
    expect(list.map((el) => el.textContent.trim())).toEqual(['first-list-title', 'second-list-title', 'third-list-title']);
  });

  it('shows card list items in content', () => {
    const list = [].slice.call(fixture.nativeElement.querySelectorAll('.card .card-text p:first-child + p + ul li'));
    expect(list.map((el) => el.textContent.trim())).toEqual([
      'first-list-item1', 'first-list-item2', 'third-list-item1', 'third-list-item2', 'third-list-item3'
    ]);
  });

  it('shows three card action bars', () => {
    const list = [].slice.call(fixture.nativeElement.querySelectorAll('.card .actions-bar'));
    expect(list.length).toEqual(3);
  });

  it('shows card links in action bars', () => {
    const list = [].slice.call(fixture.nativeElement.querySelectorAll('.card .actions-bar .col:first-child .links a'));
    expect(list.map((link) => link.textContent.trim())).toEqual(['first-link-1', 'third-link-1', 'third-link-2']);
    expect(list.map((link) => link.getAttribute('href'))).toEqual(['#first-link-1', '#third-link-1', '#third-link-2']);
  });

  it('shows card buttons in action bars', () => {
    const list = [].slice.call(fixture.nativeElement.querySelectorAll('.card .actions-bar .col:last-child .buttons .btn'));
    expect(list.map((link) => link.textContent.trim())).toEqual(['first-link-1', 'third-link-1', 'third-link-2']);
    expect(list.map((link) => link.getAttribute('href'))).toEqual(['#first-link-1', '#third-link-1', '#third-link-2']);
  });
});
