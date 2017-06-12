import { TestBed, async, ComponentFixture } from '@angular/core/testing';
import {TranslateModule, TranslateLoader} from '@ngx-translate/core';
import {TranslateService, LangChangeEvent} from '@ngx-translate/core';
import { HttpModule, Http } from '@angular/http';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {MissingTranslationHandler} from '@ngx-translate/core';
import {DebugElement} from '@angular/core';

import { AppComponent} from './app.component';
import missingTranslationHandler from './app.missingTranslation';

export function HttpLoaderFactory(http: Http) {
  return new TranslateHttpLoader(http, '/base/src/assets/i18n/', '.json');
}

/* ATTENTION Angular 2 zone.js reset created component after each it section */
const resetTestBed = TestBed.resetTestingModule;
TestBed.resetTestingModule = () => {
  return TestBed;
};

describe('When initializing AppComponent', function () {
  let fixture: ComponentFixture<AppComponent>;
  let app: DebugElement;
  let defaultLang;
  let langChangeSubscription;

  beforeAll((done) => TestBed.configureTestingModule({
      imports: [HttpModule, TranslateModule.forRoot({
        missingTranslationHandler,
        loader: {
          provide: TranslateLoader,
          useFactory: HttpLoaderFactory,
          deps: [Http]
        }
      })],
      declarations: [
        AppComponent
      ]
    })
    .overrideComponent(AppComponent, {
      set: {
        template: '<h1>{{ title | translate }}</h1>'
      }
    })
    .compileComponents()
    .then(() => {
      fixture = TestBed.createComponent(AppComponent);
    })
    .then(() => new Promise((resolve) => {
      const translate: TranslateService = TestBed.get(TranslateService);
      langChangeSubscription = translate.onLangChange.subscribe((event: LangChangeEvent) => {
        defaultLang = event.lang;
        resolve();
      });
    }))
    .then(() => {
      fixture.autoDetectChanges(true);
      app = fixture.debugElement;
    })
    .then(done));

  afterAll(() => {
    langChangeSubscription.unsubscribe();
    resetTestBed();
    TestBed.resetTestingModule = resetTestBed;
  });

  it('creates the app', function () {
    expect(app.componentInstance).toBeTruthy();
  });

  it('loads english as default language', function () {
    expect(defaultLang).toEqual('en');
  });

  it(`sets 'title' into app.title`, function () {
    expect(app.componentInstance.title).toEqual('title');
  });

  it('shows translated title', function () {
    expect(app.nativeElement.querySelector('h1').textContent).toEqual('Hello from JSON');
  });

  describe('and then changing title to undefined', function () {
    beforeAll(() => {
      app.componentInstance.title = 'not title key';
      fixture.detectChanges();
    });

    it(`shows 'Translation not found'`, () => {
      expect(app.nativeElement.querySelector('h1').textContent).toContain('Translation not found');
    });
  });
});
