import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {ReactiveFormsModule} from '@angular/forms';
import {RouterModule, Routes} from '@angular/router';
import {StoreModule} from '@ngrx/store';
import {EffectsModule} from '@ngrx/effects';
import {FowOverallComponent} from './components/fow-overall/fow-overall.component';
import {RecordEffects} from './effects/record.effects';
import {
  loadByGroundFowReducer,
  loadByHostFowReducer,
  loadByMatchFowReducer,
  loadByOppositionFowReducer,
  loadBySeasonFowReducer,
  loadBySeriesFowReducer,
  loadByYearFowReducer,
  loadInnByInnFowReducer,
  loadOverallFowReducer
} from './reducers/record.reducer';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {FowInnByInnComponent} from './components/fow-inn-by-inn/fow-inn-by-inn.component';
import {MatchTotalsComponent} from './components/match-totals/match-totals.component';
import {SeriesAveragesComponent} from './components/series-averages/series-averages.component';
import {GroundAveragesComponent} from './components/ground-averages/ground-averages.component';
import {ByHostComponent} from './components/by-host/by-host.component';
import {ByOppositionComponent} from './components/by-opposition/by-opposition.component';
import {ByYearOfMatchStartComponent} from './components/by-year-of-match-start/by-year-of-match-start.component';
import {BySeasonComponent} from './components/by-season/by-season.component';
import {SharedModule} from '../shared/shared.module';
import {FowAggregateRecordUiComponent} from './components/fow-aggregate-record-ui/fow-aggregate-record-ui.component';
import {FowByInningsUiComponent} from './components/fow-by-innings-ui/fow-by-innings-ui.component';
import {GetFowRecordsComponent} from './components/get-fow-records/get-fow-records.component';
import {RadioButtonModule} from "primeng/radiobutton";
import {ToggleSwitchModule} from "primeng/toggleswitch";


const routes: Routes = [
  {path: 'records/fowrecords', component: GetFowRecordsComponent},
  {path: 'records/fowrecords/overall', component: FowOverallComponent},
  {path: 'records/fowrecords/inningsbyinnings', component: FowInnByInnComponent},
  {path: 'records/fowrecords/inningsbyinningsforwicket', component: FowInnByInnComponent},
  {path: 'records/fowrecords/matchtotals', component: MatchTotalsComponent},
  {path: 'records/fowrecords/seriesaverages', component: SeriesAveragesComponent},
  {path: 'records/fowrecords/groundaverages', component: GroundAveragesComponent},
  {path: 'records/fowrecords/bycountry', component: ByHostComponent},
  {path: 'records/fowrecords/byopposition', component: ByOppositionComponent},
  {path: 'records/fowrecords/byyearofmatchstart', component: ByYearOfMatchStartComponent},
  {path: 'records/fowrecords/byseason', component: BySeasonComponent},
];


@NgModule({
  exports: [RouterModule, GetFowRecordsComponent],
  declarations: [
    GetFowRecordsComponent,
    FowOverallComponent,
    FowInnByInnComponent,
    MatchTotalsComponent,
    SeriesAveragesComponent,
    GroundAveragesComponent,
    ByHostComponent,
    ByOppositionComponent,
    ByYearOfMatchStartComponent,
    BySeasonComponent,
    FowAggregateRecordUiComponent,
    FowByInningsUiComponent
  ], imports: [SharedModule,
    CommonModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
    StoreModule.forFeature('fowrecords', {
      overall: loadOverallFowReducer,
      inningsByInnings: loadInnByInnFowReducer,
      byMatch: loadByMatchFowReducer,
      bySeries: loadBySeriesFowReducer,
      byGround: loadByGroundFowReducer,
      byHost: loadByHostFowReducer,
      byOpposition: loadByOppositionFowReducer,
      byYear: loadByYearFowReducer,
      bySeason: loadBySeasonFowReducer
    }),
    EffectsModule.forFeature([RecordEffects]),
    RadioButtonModule,
    ToggleSwitchModule,
    FontAwesomeModule,
  ], providers: []
})
export class FowRecordsModule {
}
