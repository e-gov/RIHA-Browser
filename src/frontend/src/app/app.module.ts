import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule, Http } from '@angular/http';
import {TranslateModule, TranslateLoader} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {TranslateService} from '@ngx-translate/core';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

import missingTranslationHandler from './app.missingTranslation';

import { AppComponent } from './app.component';
import { RihaNavbarComponent } from './riha-navbar/riha-navbar.component';
import { JsonDataService } from './json-data.service';
import { CardDeckComponent } from './card-deck/card-deck.component';
import { ProducerListComponent } from './producer-list/producer-list.component';
import { ProducerDetailsComponent } from './producer-details/producer-details.component';
import { ApproverListComponent } from './approver-list/approver-list.component';
import { ApproverDetailsComponent } from './approver-details/approver-details.component';
import { BrowserListComponent } from './browser-list/browser-list.component';

export function HttpLoaderFactory(http: Http) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    RihaNavbarComponent,
    CardDeckComponent,
    ProducerListComponent,
    ProducerDetailsComponent,
    ApproverListComponent,
    ApproverDetailsComponent,
    BrowserListComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    TranslateModule.forRoot({
      missingTranslationHandler,
      loader: {
        provide: TranslateLoader,
        useFactory: (HttpLoaderFactory),
        deps: [Http]
      }
    }),
    NgbModule.forRoot()
  ],
  bootstrap: [AppComponent, RihaNavbarComponent, CardDeckComponent],
  providers: [JsonDataService]
})

export class AppModule {}
