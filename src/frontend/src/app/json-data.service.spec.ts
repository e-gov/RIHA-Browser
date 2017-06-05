import { TestBed, inject } from '@angular/core/testing';
import { InMemoryWebApiModule, InMemoryBackendConfig } from 'angular-in-memory-web-api';
import {JsonData} from './app.json-data.memory-web-api';
import { JsonDataService } from './json-data.service';
import { HttpModule} from '@angular/http';

describe('When initializing JsonDataService', function () {
  let resetTestBed;
  let service;
  let routes;
  let cards;

  beforeAll((done) => {
    new Promise((resolve) => {
      resetTestBed = TestBed.resetTestingModule;
      TestBed.resetTestingModule = () => {
        return TestBed;
      };
      TestBed.configureTestingModule({
        imports: [HttpModule, InMemoryWebApiModule.forRoot(JsonData, {apiBase: '', rootPath: '/assets/'})],
        providers: [JsonDataService]
      });
      service = TestBed.get(JsonDataService);
      service.routes.subscribe(function (_data) {
        routes = _data;
      });
      service.cards.subscribe(function (_data) {
        cards = _data;
        resolve();
      });
    }).then(done);
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

  it('fires routes', () => {
    expect(routes).toEqual([{i18n: 'i18n', hash: '#hash'}]);
  });

  it('fires cards', () => {
    expect(cards).toEqual([{}, {}, {}]);
  });
});
