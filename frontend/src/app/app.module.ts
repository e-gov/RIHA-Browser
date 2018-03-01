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
import { CustomFormsModule } from 'ng2-validation';
import { HttpInterceptorModule } from 'ng-http-interceptor';
import { UiSwitchModule } from 'ngx-ui-switch/src';

import missingTranslationHandler from './app.missingTranslation';

import { AppComponent } from './app.component';
import { RihaNavbarComponent } from './components/riha-navbar/riha-navbar.component';

//services
import { SystemsService } from './services/systems.service';
import { WindowRefService } from './services/window-ref.service';
import { EnvironmentService } from './services/environment.service';
import { GeneralHelperService } from './services/general-helper.service';
import { SessionHelperService } from './services/session-helper.service';
import { ModalHelperService } from './services/modal-helper.service';


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
import { ProducerDetailsIssuesComponent } from './components/producer-details/producer-details-issues/producer-details-issues.component';
import { ApproverAddIssueComponent } from './components/approver-add-issue/approver-add-issue.component';
import { ApproverIssueDetailsComponent } from './components/approver-issue-details/approver-issue-details.component';
import { ActiveOrganizationChooserComponent } from './components/active-organization-chooser/active-organization-chooser.component';
import { TooltipComponent } from './components/tooltip/tooltip.component';
import { ProducerDetailsContactsComponent } from './components/producer-details/producer-details-contacts/producer-details-contacts.component';
import { ProducerEditContactsComponent } from './components/producer-edit/producer-edit-contacts/producer-edit-contacts.component';
import { WarningModalComponent } from './components/session-timeout/warning-modal/warning-modal.component';
import { InfoModalComponent } from './components/session-timeout/info-modal/info-modal.component';
import { ProducerDetailsRelationsComponent } from './components/producer-details/producer-details-relations/producer-details-relations.component';
import { ProducerEditRelationsComponent } from './components/producer-edit/producer-edit-relations/producer-edit-relations.component';
import { GridTotalFoundComponent } from './components/grid-view/grid-total-found/grid-total-found.component';
import { GridCurrentlyShowingComponent } from './components/grid-view/grid-currently-showing/grid-currently-showing.component';
import { SortButtonComponent } from './components/grid-view/sort-button/sort-button.component';
import { ProducerDetailsSecurityComponent } from './components/producer-details/producer-details-security/producer-details-security.component';
import { ProducerEditSecurityComponent } from './components/producer-edit/producer-edit-security/producer-edit-security.component';
import { FileIconComponent } from './components/files-related/file-icon/file-icon.component';
import { FileHintComponent } from './components/files-related/file-hint/file-hint.component';
import { ApproverDashboardComponent } from './components/approver-dashboard/approver-dashboard.component';
import { SystemsForApprovalListComponent } from './components/approver-dashboard/systems-for-approval-list/systems-for-approval-list.component';
import { ActiveDiscussionsComponent } from './components/approver-dashboard/active-discussions/active-discussions.component';
import { DiscussionsListComponent } from './components/approver-dashboard/active-discussions/discussions-list/discussions-list.component';
import { BrowserFilesListComponent } from './components/browser-list/browser-files-list/browser-files-list.component';

//pipes
import { DatemPipe } from './pipes/datem.pipe';
import { LinkifyPipe } from './pipes/linkify.pipe';

export function HttpLoaderFactory(http: Http) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

export function onApplicationStart(environmentService: EnvironmentService){
  return () => environmentService.onAppStart();
}

const routes: Routes = [
  { path: '', redirectTo: 'Avaleht', pathMatch: 'full' },
  { path: 'Avaleht', component: FrontPageComponent },
  { path: 'Home', component: FrontPageComponent },
  { path: 'Login', component: LoginFormComponent },
  { path: 'Infosüsteemid', component: BrowserListComponent },
  { path: 'Andmeobjektid', component: BrowserFilesListComponent },
  { path: 'Systems', component: BrowserListComponent },
  { path: 'Kirjelda', component: ProducerListComponent },
  { path: 'Describe', component: ProducerListComponent },
  { path: 'Infosüsteemid/Vaata/:reference', component: ProducerDetailsComponent },
  { path: 'Infosüsteemid/Vaata/:reference/Arutelu/:issue_id', component: ProducerDetailsComponent },
  { path: 'Systems/Vaata/:reference', component: ProducerDetailsComponent },
  { path: 'Kirjelda/Vaata/:reference', component: ProducerDetailsComponent },
  { path: 'Describe/View/:reference', component: ProducerDetailsComponent },
  { path: 'Kirjelda/Muuda/:reference', component: ProducerEditComponent },
  { path: 'Describe/Edit/:reference', component: ProducerEditComponent },
  { path: 'Kirjelda/Uus', component: ProducerAddComponent },
  { path: 'Describe/New', component: ProducerAddComponent },
  { path: 'Hinda', component: ApproverDashboardComponent },
  { path: 'Approve', component: ApproverListComponent },
  { path: 'Hinda/Detailid/:short_name', component: ApproverDetailsComponent },
  { path: 'Approve/Details/:short_name', component: ApproverDetailsComponent },
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
    ProducerDetailsIssuesComponent,
    ApproverAddIssueComponent,
    ApproverIssueDetailsComponent,
    ActiveOrganizationChooserComponent,
    TooltipComponent,
    ProducerDetailsContactsComponent,
    ProducerEditContactsComponent,
    WarningModalComponent,
    InfoModalComponent,
    ProducerDetailsRelationsComponent,
    ProducerEditRelationsComponent,
    GridTotalFoundComponent,
    GridCurrentlyShowingComponent,
    SortButtonComponent,
    ProducerDetailsSecurityComponent,
    ProducerEditSecurityComponent,
    DatemPipe,
    FileIconComponent,
    FileHintComponent,
    ApproverDashboardComponent,
    SystemsForApprovalListComponent,
    ActiveDiscussionsComponent,
    DiscussionsListComponent,
    LinkifyPipe,
    BrowserFilesListComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    TagInputModule,
    BrowserAnimationsModule,
    CustomFormsModule,
    RouterModule.forRoot(routes),
    HttpInterceptorModule,
    UiSwitchModule ,
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
    ApproverAddIssueComponent,
    ApproverIssueDetailsComponent,
    ActiveOrganizationChooserComponent,
    ProducerEditContactsComponent,
    InfoModalComponent,
    WarningModalComponent,
    ProducerEditRelationsComponent,
    ProducerEditSecurityComponent
  ],
  bootstrap: [AppComponent],
  providers: [
    SystemsService,
    WindowRefService,
    EnvironmentService,
    GeneralHelperService,
    SessionHelperService,
    ModalHelperService,
    { provide: APP_INITIALIZER, useFactory: onApplicationStart, deps: [EnvironmentService], multi: true }]
})

export class AppModule {}
