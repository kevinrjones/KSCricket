import {APP_ID, APP_INITIALIZER, inject, NgModule, provideAppInitializer} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {RouterModule} from '@angular/router';

import {StoreModule} from '@ngrx/store';
import {EffectsModule} from '@ngrx/effects';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';

import {AppComponent} from './app.component';
import {HomeComponent} from './components/home/home.component';
import {environment} from '../environments/environment';
import {PageNotFoundComponent} from './components/pagenotfound/pagenotfound.component';
import {NavMenuComponent} from './components/nav-menu/nav-menu.component';
import {countryReducer} from './reducers/countries.reducer';
import {PlayerModule} from './modules/player/player.module';
import {ScorecardModule} from './modules/scorecard/scorecard-module';
import {BattingRecordsModule} from './modules/batting-records/batting-records.module';
import {teamReducer} from './reducers/teams.reducer';
import {TeamEffects} from './effects/team.effects';
import {CountryEffects} from './effects/country.effects';
import {GroundEffects} from './effects/ground.effects';
import {groundsReducer} from './reducers/grounds.reducer';
import {matchDatesReducer, seriesDatesReducer} from './reducers/dates.reducer';
import {DateEffects} from './effects/date.effects';
import {recordSummaryReducer} from './reducers/recordsummary.reducer';
import {RecordSummaryEffects} from './effects/recordsummary.effects';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {BowlingRecordsModule} from './modules/bowling-records/bowling-records.module';
import {HeaderComponent} from './components/header/header.component';
import {MatchSubTypeEffects} from './effects/match-sub-type.effects';
import {matchSubTypeReducer} from './reducers/match-sub-type.reducer';
import {loadSearchFormStateReducer} from './reducers/form-state.reducer';
import {setErrorStateReducer} from './reducers/error-state.reducer';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpRequestInterceptor} from './interceptors/http-request.interceptor';
import {loadingStateReducer} from './reducers/loading.reducer';
import {TeamRecordsModule} from './modules/teams-records/teams-records.module';
import {FieldingRecordsModule} from './modules/fielding-records/fielding-records.module';
import {FowRecordsModule} from './modules/fow-records/fow-records.module';
import {CsrfHeaderInterceptor} from './interceptors/csrf-header.interceptor';
import {HttpErrorInterceptor} from './interceptors/http-error.interceptor';
import {SearchModule} from "./modules/search/search-module";
import {provideLumberjack} from "@ngworker/lumberjack";
import {provideLumberjackConsoleDriver} from "@ngworker/lumberjack/console-driver";
import {provideAnimationsAsync} from "@angular/platform-browser/animations/async";
import {providePrimeNG} from "primeng/config";
import {AcsPreset} from './acspreset';
import {Menubar} from "primeng/menubar";
import {Menu} from "primeng/menu";
import {TieredMenu} from "primeng/tieredmenu";
import {Button} from "primeng/button";
import {MessageModule} from "primeng/message";
import {MessageService} from "primeng/api";
import {TooltipModule} from "primeng/tooltip";
import {Toast} from "primeng/toast";
import {ConfigService} from "./services/config.service";

export function initializeApp(configService: ConfigService) {
  return configService.loadConfig();
}

@NgModule({
  declarations: [
    AppComponent,
    NavMenuComponent,
    HomeComponent,
    PageNotFoundComponent,
    HeaderComponent
  ],
  exports: [],
  bootstrap: [AppComponent],
  imports: [
    MessageModule,
    TooltipModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    RouterModule.forRoot([
      {path: '', component: HomeComponent, pathMatch: 'full'},
      {path: '**', component: PageNotFoundComponent, pathMatch: 'full'},
    ]),
    StoreModule.forRoot({
      countries: countryReducer,
      teams: teamReducer,
      grounds: groundsReducer,
      seriesDates: seriesDatesReducer,
      matchDates: matchDatesReducer,
      matchSubTypes: matchSubTypeReducer,
      playerRecordSummary: recordSummaryReducer,
      formState: loadSearchFormStateReducer,
      errorState: setErrorStateReducer,
      loading: loadingStateReducer
    }, {}),
    EffectsModule.forRoot([TeamEffects, CountryEffects, GroundEffects, DateEffects, RecordSummaryEffects, MatchSubTypeEffects]),
    StoreDevtoolsModule.instrument({maxAge: 25, logOnly: environment.production}),
    FontAwesomeModule,
    PlayerModule,
    SearchModule,
    ScorecardModule,
    BattingRecordsModule,
    BowlingRecordsModule,
    TeamRecordsModule,
    FieldingRecordsModule,
    FowRecordsModule,
    MessageModule,
    Menubar,
    Menu,
    TieredMenu,
    Button,
    Toast],
  providers: [
    // todo: not sure I need this, see https://stackoverflow.com/questions/76452844/angular-universal-browsermodule-withservertransition-is-deprecated-what-is-the
    {
      provide: APP_ID,
      useValue: 'acs-cricket'
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpErrorInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpRequestInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: CsrfHeaderInterceptor,
      multi: true
    },
    provideAppInitializer(() => initializeApp(inject(ConfigService))),
    MessageService,
    provideHttpClient(withInterceptorsFromDi()),
    provideLumberjack(),
    provideLumberjackConsoleDriver(),
    provideAnimationsAsync(),
    providePrimeNG({
      theme: {
        preset: AcsPreset,
        options: {
          darkModeSelector: 'none'
        }
      }
    })
  ]
})
export class AppModule {
}
