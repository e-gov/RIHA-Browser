import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule, Http } from '@angular/http';
import { TranslateModule, TranslateLoader} from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateService } from '@ngx-translate/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RouterModule, Routes } from '@angular/router';
import { TagInputModule } from 'ng2-tag-input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import missingTranslationHandler from './app.missingTranslation';

import { AppComponent } from './app.component';
import { RihaNavbarComponent } from './components/riha-navbar/riha-navbar.component';
import { JsonDataService } from './json-data.service';

//services
import { SystemsService } from './services/systems.service';
import { EnvironmentService } from './services/environment.service';

//components
import { CardDeckComponent } from './components/card-deck/card-deck.component';
import { ProducerListComponent } from './components/producer-list/producer-list.component';
import { ApproverListComponent } from './components/approver-list/approver-list.component';
import { ApproverDetailsComponent } from './components/approver-details/approver-details.component';
import { BrowserListComponent } from './components/browser-list/browser-list.component';
import { FrontPageComponent } from './components/front-page/front-page.component';
import { PageHeaderComponent } from './components/page-header/page-header.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { LoginFormComponent } from './components/login-form/login-form.component';
import { ProducerAddComponent } from './components/producer-add/producer-add.component';
import { ProducerDetailsComponent } from './components/producer-details/producer-details.component';
import { ProducerEditComponent } from './components/producer-edit/producer-edit.component';
import { DateRowComponent } from './components/date-row/date-row.component';

export function HttpLoaderFactory(http: Http) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

export function setGlobalEnvironment(environmentService: EnvironmentService){
  return () => environmentService.load();
}

const routes: Routes = [
  { path: '', redirectTo: 'Avaleht', pathMatch: 'full' },
  { path: 'Avaleht', component: FrontPageComponent },
  { path: 'Home', component: FrontPageComponent },
  { path: 'Login', component: LoginFormComponent },
  { path: 'Infosüsteemid', component: BrowserListComponent },
  { path: 'Systems', component: BrowserListComponent },
  { path: 'Kirjelda', component: ProducerListComponent },
  { path: 'Describe', component: ProducerListComponent },
  { path: 'Kirjelda/Vaata/:id', component: ProducerDetailsComponent },
  { path: 'Describe/View/:id', component: ProducerDetailsComponent },
  { path: 'Kirjelda/Muuda/:id', component: ProducerEditComponent },
  { path: 'Describe/Edit/:id', component: ProducerEditComponent },
  { path: 'Kirjelda/Uus', component: ProducerAddComponent },
  { path: 'Describe/New', component: ProducerAddComponent },
  { path: 'Hinda', component: ApproverListComponent },
  { path: 'Approve', component: ApproverListComponent },
  { path: 'Hinda/Detailid/:id', component: ApproverDetailsComponent },
  { path: 'Approve/Details/:id', component: ApproverDetailsComponent },
  { path: 'Hinda/Detailid', component: ApproverDetailsComponent },
  { path: 'Approve/Details', component: ApproverDetailsComponent },
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    RihaNavbarComponent,
    CardDeckComponent,
    ProducerListComponent,
    ProducerDetailsComponent,
    ApproverListComponent,
    ApproverDetailsComponent,
    BrowserListComponent,
    FrontPageComponent,
    PageHeaderComponent,
    PageNotFoundComponent,
    LoginFormComponent,
    ProducerAddComponent,
    ProducerEditComponent,
    DateRowComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    TagInputModule,
    BrowserAnimationsModule,
    RouterModule.forRoot(routes),
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
  bootstrap: [AppComponent],
  providers: [JsonDataService, SystemsService, EnvironmentService, { provide: APP_INITIALIZER, useFactory: setGlobalEnvironment, deps: [EnvironmentService], multi: true }]
})

export class AppModule {}
