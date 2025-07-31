import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {ReactiveFormsModule} from '@angular/forms';
import {RouterModule, Routes} from '@angular/router';
import {StoreModule} from '@ngrx/store';
import {EffectsModule} from '@ngrx/effects';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {GetBowlingRecordsComponent} from './components/get-bowling-records/get-bowling-records.component';
import {RecordEffects} from './effects/record.effects';
import {SharedModule} from '../shared/shared.module';
import {BowlingOverallComponent} from './components/bowling-overall/bowling-overall.component';
import {
  loadByGroundBowlingReducer,
  loadByHostBowlingReducer,
  loadByMatchBowlingReducer,
  loadByOppositionBowlingReducer,
  loadBySeasonBowlingReducer,
  loadBySeriesBowlingReducer,
  loadByYearBowlingReducer,
  loadInnByInnBowlingReducer,
  loadOverallBowlingReducer
} from './reducers/record.reducer';
import {BowlingInnByInnComponent} from './components/bowling-inn-by-inn/bowling-inn-by-inn.component';
import {MatchTotalsComponent} from './components/match-totals/match-totals.component';
import {SeriesAveragesComponent} from './components/series-averages/series-averages.component';
import {GroundAveragesComponent} from './components/ground-averages/ground-averages.component';
import {ByOppositionComponent} from './components/by-opposition/by-opposition.component';
import {ByHostComponent} from './components/by-host/by-host.component';
import {ByYearOfMatchStartComponent} from './components/by-year-of-match-start/by-year-of-match-start.component';
import {BySeasonComponent} from './components/by-season/by-season.component';
import {BowlingByInningsUiComponent} from './components/bowling-by-innings-ui/bowling-by-innings-ui.component';
import {
  BowlingAggregateRecordUiComponent
} from './components/bowling-aggregate-record-ui/bowling-aggregate-record-ui.component';
import {RadioButton} from "primeng/radiobutton";

const routes: Routes = [
  {path: 'records/bowlingrecords', component: GetBowlingRecordsComponent},
  {path: 'records/bowlingrecords/overall', component: BowlingOverallComponent},
  {path: 'records/bowlingrecords/inningsbyinnings', component: BowlingInnByInnComponent},
  {path: 'records/bowlingrecords/matchtotals', component: MatchTotalsComponent},
  {path: 'records/bowlingrecords/seriesaverages', component: SeriesAveragesComponent},
  {path: 'records/bowlingrecords/groundaverages', component: GroundAveragesComponent},
  {path: 'records/bowlingrecords/bycountry', component: ByHostComponent},
  {path: 'records/bowlingrecords/byopposition', component: ByOppositionComponent},
  {path: 'records/bowlingrecords/byyearofmatchstart', component: ByYearOfMatchStartComponent},
  {path: 'records/bowlingrecords/byseason', component: BySeasonComponent},
];


@NgModule({
  exports: [RouterModule,
    GetBowlingRecordsComponent,
    BowlingOverallComponent
  ],
  declarations: [
    GetBowlingRecordsComponent,
    BowlingOverallComponent,
    BowlingInnByInnComponent,
    MatchTotalsComponent,
    SeriesAveragesComponent,
    GroundAveragesComponent,
    ByOppositionComponent,
    ByHostComponent,
    ByYearOfMatchStartComponent,
    BySeasonComponent,
    BowlingByInningsUiComponent,
    BowlingAggregateRecordUiComponent,
  ], imports: [SharedModule,
    CommonModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
    StoreModule.forFeature('bowlingrecords', {
      overall: loadOverallBowlingReducer,
      inningsByInnings: loadInnByInnBowlingReducer,
      byMatch: loadByMatchBowlingReducer,
      bySeries: loadBySeriesBowlingReducer,
      byGround: loadByGroundBowlingReducer,
      byHost: loadByHostBowlingReducer,
      byOpposition: loadByOppositionBowlingReducer,
      byYear: loadByYearBowlingReducer,
      bySeason: loadBySeasonBowlingReducer,
    }),
    EffectsModule.forFeature([RecordEffects]),
    FontAwesomeModule,
    RadioButton],
  providers: [
  ]
})
export class BowlingRecordsModule {
}
