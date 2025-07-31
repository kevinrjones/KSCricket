import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from '@angular/router';
import {ReactiveFormsModule} from '@angular/forms';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {StoreModule} from '@ngrx/store';
import {EffectsModule} from '@ngrx/effects';
import {
  scorecardByDecadesReducer,
  scorecardByYearReducer,
  scorecardListSuccessReducer,
  scorecardSuccessReducer,
  scorecardTournamentListSuccessReducer
} from './reducers/scorecard.reducer';
import {ScorecardEffects} from './effects/scorecard.effects';
import {ScorecardListComponent} from './components/scorecard-list/scorecardlist.component';
import {GetByDecadeComponent} from './components/get-by-decade/get-by-decade.component';
import {ScorecardByDecadeListComponent} from './components/get-by-decade/by-decade-list/by-decade-list.component';
import {GetScorecardListComponent} from './components/get-scorecard-list/get-scorecard-list.component';
import {GetTournamentListComponent} from './components/get-tournament-list/get-tournament-list.component';
import {GetCardComponent} from './components/get-card/get-card.component';
import {GetByYearComponent} from './components/get-by-year/get-by-year.component';
import {ScorecardByYearListComponent} from './components/get-by-year/by-year-list/by-year-list.component';
import {CardDisplayComponent} from './components/get-card/card-display/card-display.component';
import {GetCardByIdComponent} from './components/get-card/get-card-by-id/get-card-by-id.component';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {SharedModule} from "../shared/shared.module";


const routes: Routes = [
  {path: 'scorecard/tournament/:tournament', component: GetTournamentListComponent},
  {path: 'scorecard/card/:name', component: GetCardComponent},
  {path: 'scorecard/cardbyid/:id', component: GetCardByIdComponent},
  {path: 'scorecard/bydecade/:name', component: GetByDecadeComponent},
  {path: 'scorecard/byyear/:name/:year', component: GetByYearComponent},
  {path: 'scorecardlist', component: GetScorecardListComponent},
];

@NgModule({
  exports: [RouterModule, ScorecardListComponent, ScorecardByDecadeListComponent, GetByYearComponent],
  declarations: [
    ScorecardListComponent, CardDisplayComponent,
    ScorecardByDecadeListComponent,
    GetByYearComponent,
    ScorecardByYearListComponent,
    GetByDecadeComponent,
    GetScorecardListComponent,
    GetTournamentListComponent,
    GetCardComponent,
    GetByYearComponent,
    GetCardByIdComponent
  ], imports: [CommonModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
    StoreModule.forFeature('scorecards', {
      scorecards: scorecardListSuccessReducer,
      scorecardTournaments: scorecardTournamentListSuccessReducer,
      scorecard: scorecardSuccessReducer,
      decades: scorecardByDecadesReducer,
      tournamentsBySeason: scorecardByYearReducer,
    }),
    EffectsModule.forFeature([ScorecardEffects]),
    FontAwesomeModule,
    SharedModule],
  providers: []
})
export class ScorecardModule {
}
