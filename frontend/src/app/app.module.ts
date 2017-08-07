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
import { ToastrModule } from 'ngx-toastr';
import { Ng2PageScrollModule } from 'ng2-page-scroll';

import missingTranslationHandler from './app.missingTranslation';

import { AppComponent } from './app.component';
import { RihaNavbarComponent } from './components/riha-navbar/riha-navbar.component';
import { JsonDataService } from './json-data.service';

//services
import { SystemsService } from './services/systems.service';
import { WindowRefService } from './services/window-ref.service';
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
import { ProducerDetailsObjectsComponent } from './components/producer-details/producer-details-objects/producer-details-objects.component';
import { ProducerEditObjectsComponent } from './components/producer-edit/producer-edit-objects/producer-edit-objects.component';
import { ProducerDetailsGeneralComponent } from './components/producer-details/producer-details-general/producer-details-general.component';
import { ProducerEditGeneralComponent } from './components/producer-edit/producer-edit-general/producer-edit-general.component';
import { ProducerDetailsDocumentsComponent } from './components/producer-details/producer-details-documents/producer-details-documents.component';
import { ProducerEditDocumentsComponent } from './components/producer-edit/producer-edit-documents/producer-edit-documents.component';
import { ProducerEditLegislationsComponent } from './components/producer-edit/producer-edit-legislations/producer-edit-legislations.component';
import { ProducerDetailsLegislationsComponent } from './components/producer-details/producer-details-legislations/producer-details-legislations.component';
import { AlertComponent } from './components/alert/alert.component';
import { ProducerDetailsCommentsComponent } from './components/producer-details/producer-details-comments/producer-details-comments.component';
import { ApproverAddCommentComponent } from './components/approver-add-comment/approver-add-comment.component';

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
  { path: 'Infosüsteemid/Vaata/:id', component: ProducerDetailsComponent },
  { path: 'Systems/Vaata/:id', component: ProducerDetailsComponent },
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
    DateRowComponent,
    ProducerDetailsObjectsComponent,
    ProducerEditObjectsComponent,
    ProducerDetailsGeneralComponent,
    ProducerEditGeneralComponent,
    ProducerDetailsDocumentsComponent,
    ProducerEditDocumentsComponent,
    ProducerEditLegislationsComponent,
    ProducerDetailsLegislationsComponent,
    AlertComponent,
    ProducerDetailsCommentsComponent,
    ApproverAddCommentComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    TagInputModule,
    BrowserAnimationsModule,
    Ng2PageScrollModule.forRoot(),
    RouterModule.forRoot(routes),
    ToastrModule.forRoot(),
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
  entryComponents: [
    ProducerEditObjectsComponent,
    ProducerEditDocumentsComponent,
    ProducerEditLegislationsComponent,
    ApproverAddCommentComponent
  ],
  bootstrap: [AppComponent],
  providers: [JsonDataService, SystemsService, WindowRefService, EnvironmentService, { provide: APP_INITIALIZER, useFactory: setGlobalEnvironment, deps: [EnvironmentService], multi: true }]
})

export class AppModule {}
