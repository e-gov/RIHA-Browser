import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LangChangeEvent, TranslateLoader, TranslateModule, TranslateService } from '@ngx-translate/core';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { DebugElement } from '@angular/core';

import { AppComponent } from './app.component';
import missingTranslationHandler from './app.missingTranslation';

// Use TranslateHttpLoader directly, no factory needed with modern Angular
// The loader will be configured through the provider system
// This function is kept for backwards compatibility with tests
export function HttpLoaderFactory() {
  return new TranslateHttpLoader();
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

  beforeAll(done =>
    TestBed.configureTestingModule({
      declarations: [AppComponent],
      imports: [
        TranslateModule.forRoot({
          missingTranslationHandler,
          loader: {
            provide: TranslateLoader,
            useClass: TranslateHttpLoader,
          },
        }),
      ],
      providers: [provideHttpClient(withInterceptorsFromDi())],
    })
      .overrideComponent(AppComponent, {
        set: {
          template: '<h1>{{ title | translate }}</h1>',
        },
      })
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(AppComponent);
      })
      .then(
        () =>
          new Promise(resolve => {
            const translate: TranslateService = TestBed.inject(TranslateService);
            langChangeSubscription = translate.onLangChange.subscribe((event: LangChangeEvent) => {
              defaultLang = event.lang;
              resolve(void 0);
            });
          }),
      )
      .then(() => {
        fixture.autoDetectChanges(true);
        app = fixture.debugElement;
      })
      .then(done),
  );

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
