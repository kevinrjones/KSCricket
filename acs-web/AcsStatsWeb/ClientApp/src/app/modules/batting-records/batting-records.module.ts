import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {GetBattingRecordsComponent} from './components/get-batting-records/get-batting-records.component';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {ReactiveFormsModule} from '@angular/forms';
import {RouterModule, Routes} from '@angular/router';
import {StoreModule} from '@ngrx/store';
import {EffectsModule} from '@ngrx/effects';
import {BattingOverallComponent} from './components/batting-overall/batting-overall.component';
import {RecordEffects} from './effects/record.effects';
import {
  loadByGroundBattingReducer,
  loadByHostBattingReducer,
  loadByMatchBattingReducer,
  loadByOppositionBattingReducer,
  loadBySeasonBattingReducer,
  loadBySeriesBattingReducer,
  loadByYearBattingReducer,
  loadInnByInnBattingReducer,
  loadOverallBattingReducer
} from './reducers/record.reducer';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {BattingInnByInnComponent} from './components/batting-inn-by-inn/batting-inn-by-inn.component';
import {MatchTotalsComponent} from './components/match-totals/match-totals.component';
import {SeriesAveragesComponent} from './components/series-averages/series-averages.component';
import {GroundAveragesComponent} from './components/ground-averages/ground-averages.component';
import {ByHostComponent} from './components/by-host/by-host.component';
import {ByOppositionComponent} from './components/by-opposition/by-opposition.component';
import {ByYearOfMatchStartComponent} from './components/by-year-of-match-start/by-year-of-match-start.component';
import {BySeasonComponent} from './components/by-season/by-season.component';
import {SharedModule} from '../shared/shared.module';
import {
  BattingAggregateRecordUiComponent
} from './components/batting-aggregate-record-ui/batting-aggregate-record-ui.component';
import {BattingByInningsUiComponent} from './components/batting-by-innings-ui/batting-by-innings-ui.component';
import {RadioButton} from "primeng/radiobutton";


const routes: Routes = [
  {path: 'records/battingrecords', component: GetBattingRecordsComponent},
  {path: 'records/battingrecords/overall', component: BattingOverallComponent},
  {path: 'records/battingrecords/inningsbyinnings', component: BattingInnByInnComponent},
  {path: 'records/battingrecords/matchtotals', component: MatchTotalsComponent},
  {path: 'records/battingrecords/seriesaverages', component: SeriesAveragesComponent},
  {path: 'records/battingrecords/groundaverages', component: GroundAveragesComponent},
  {path: 'records/battingrecords/bycountry', component: ByHostComponent},
  {path: 'records/battingrecords/byopposition', component: ByOppositionComponent},
  {path: 'records/battingrecords/byyearofmatchstart', component: ByYearOfMatchStartComponent},
  {path: 'records/battingrecords/byseason', component: BySeasonComponent},
];


@NgModule({
  exports: [RouterModule, GetBattingRecordsComponent],
  declarations: [
    GetBattingRecordsComponent,
    BattingOverallComponent,
    BattingInnByInnComponent,
    MatchTotalsComponent,
    SeriesAveragesComponent,
    GroundAveragesComponent,
    ByHostComponent,
    ByOppositionComponent,
    ByYearOfMatchStartComponent,
    BySeasonComponent,
    BattingAggregateRecordUiComponent,
    BattingByInningsUiComponent
  ], imports: [SharedModule,
    CommonModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
    StoreModule.forFeature('battingrecords', {
      overall: loadOverallBattingReducer,
      inningsByInnings: loadInnByInnBattingReducer,
      byMatch: loadByMatchBattingReducer,
      bySeries: loadBySeriesBattingReducer,
      byGround: loadByGroundBattingReducer,
      byHost: loadByHostBattingReducer,
      byOpposition: loadByOppositionBattingReducer,
      byYear: loadByYearBattingReducer,
      bySeason: loadBySeasonBattingReducer
    }),
    EffectsModule.forFeature([RecordEffects]),
    FontAwesomeModule,
    RadioButton],
  providers: []
})
export class BattingRecordsModule {
}
