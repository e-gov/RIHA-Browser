import { TestBed, inject } from '@angular/core/testing';
import { InMemoryWebApiModule, InMemoryBackendConfig } from 'angular-in-memory-web-api';
import {JsonData} from './app.json-data.memory-web-api';
import { JsonDataService } from './json-data.service';
import { HttpModule} from '@angular/http';

describe('When initializing JsonDataService', function () {
  let resetTestBed;
  let service;
  beforeAll(() => {
    resetTestBed = TestBed.resetTestingModule;
    TestBed.resetTestingModule = () => {
      return TestBed;
    };
    TestBed.configureTestingModule({
      imports: [HttpModule, InMemoryWebApiModule.forRoot(JsonData, {apiBase: '', rootPath: '/assets/'})],
      providers: [JsonDataService]
    });
    service = TestBed.get(JsonDataService);
    service.cards.subscribe(function (_data) {console.log('CARDS2', _data);});
  });

  afterAll(() => {
    resetTestBed();
    TestBed.resetTestingModule = resetTestBed;
  });

  it('creates JsonDataService', () => {
    expect(service).toBeTruthy();
  });

  it('creates JsonDataService with routes', () => {
    expect(service.routes).toBeTruthy();
  });

  xdescribe('and then getting routes data', () => {
    let routes;
    beforeAll((done) => {
      new Promise((resolve) => {
        service.routes.last.subscribe(function (_data) {
          routes = _data;
          resolve();
        });
      }).then(done);
    });

    it('fires routes', () => {
      expect(routes).toEqual([{i18n: 'i18n', hash: '#hash'}]);
    });
  });

  xdescribe('and then getting cards data', () => {
    let cards;
    beforeAll((done) => {
      new Promise((resolve) => {
        service.cards.subscribe(function (_data) {console.log('CARDS', _data)
          cards = _data;
          resolve();
        });
      }).then(done);
    });

    it('fires cards', () => {
      expect(cards).toEqual([{}, {}, {}]);
    });
  });
});
