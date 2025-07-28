import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {ReactiveFormsModule} from '@angular/forms';
import {RouterModule, Routes} from '@angular/router';
import {StoreModule} from '@ngrx/store';
import {EffectsModule} from '@ngrx/effects';
import {SharedModule} from '../shared/shared.module';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {RecordEffects} from './effects/record.effects';
import {GetFieldingRecordsComponent} from './components/get-fielding-records/get-fielding-records.component';
import {
  loadByGroundFieldingReducer,
  loadByHostFieldingReducer,
  loadByMatchFieldingReducer,
  loadByOppositionFieldingReducer,
  loadBySeasonFieldingReducer,
  loadBySeriesFieldingReducer,
  loadByYearFieldingReducer,
  loadInnByInnFieldingReducer,
  loadOverallFieldingReducer
} from './reducers/record.reducer';
import {
  FieldingAggregateRecordUiComponent
} from './components/fielding-aggregate-record-ui/fielding-aggregate-record-ui.component';
import {FieldingOverallComponent} from './components/fielding-overall/fielding-overall.component';
import {SeriesAveragesComponent} from './components/series-averages/series-averages.component';
import {GroundAveragesComponent} from './components/ground-averages/ground-averages.component';
import {ByHostComponent} from './components/by-host/by-host.component';
import {ByOppositionComponent} from './components/by-opposition/by-opposition.component';
import {ByYearOfMatchStartComponent} from './components/by-year-of-match-start/by-year-of-match-start.component';
import {BySeasonComponent} from './components/by-season/by-season.component';
import {FieldingByInningsUiComponent} from './components/fielding-by-innings-ui/fielding-by-innings-ui.component';
import {FieldingInnByInnComponent} from './components/fielding-inn-by-inn/fielding-inn-by-inn.component';
import {MatchTotalsComponent} from './components/match-totals/match-totals.component';
import {RadioButton} from "primeng/radiobutton";


const routes: Routes = [
  {path: 'records/fieldingrecords', component: GetFieldingRecordsComponent},
  {path: 'records/fieldingrecords/overall', component: FieldingOverallComponent},
  {path: 'records/fieldingrecords/inningsbyinnings', component: FieldingInnByInnComponent},
  {path: 'records/fieldingrecords/matchtotals', component: MatchTotalsComponent},
  {path: 'records/fieldingrecords/seriesaverages', component: SeriesAveragesComponent},
  {path: 'records/fieldingrecords/groundaverages', component: GroundAveragesComponent},
  {path: 'records/fieldingrecords/bycountry', component: ByHostComponent},
  {path: 'records/fieldingrecords/byopposition', component: ByOppositionComponent},
  {path: 'records/fieldingrecords/byyearofmatchstart', component: ByYearOfMatchStartComponent},
  {path: 'records/fieldingrecords/byseason', component: BySeasonComponent},
];


@NgModule({
  exports: [RouterModule],
  declarations: [
    GetFieldingRecordsComponent,
    FieldingAggregateRecordUiComponent,
    FieldingByInningsUiComponent,
    FieldingOverallComponent,
    SeriesAveragesComponent,
    GroundAveragesComponent,
    ByHostComponent,
    ByOppositionComponent,
    ByYearOfMatchStartComponent,
    BySeasonComponent,
    FieldingInnByInnComponent,
    MatchTotalsComponent
  ], imports: [SharedModule,
    CommonModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
    StoreModule.forFeature('fieldingrecords', {
      overall: loadOverallFieldingReducer,
      inningsByInnings: loadInnByInnFieldingReducer,
      byMatch: loadByMatchFieldingReducer,
      bySeries: loadBySeriesFieldingReducer,
      byGround: loadByGroundFieldingReducer,
      byHost: loadByHostFieldingReducer,
      byOpposition: loadByOppositionFieldingReducer,
      byYear: loadByYearFieldingReducer,
      bySeason: loadBySeasonFieldingReducer
    }),
    EffectsModule.forFeature([RecordEffects]),
    FontAwesomeModule,
    RadioButton],
  providers: []
})
export class FieldingRecordsModule {
}
