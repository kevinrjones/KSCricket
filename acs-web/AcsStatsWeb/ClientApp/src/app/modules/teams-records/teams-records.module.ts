import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {ReactiveFormsModule} from '@angular/forms';
import {RouterModule, Routes} from '@angular/router';
import {StoreModule} from '@ngrx/store';
import {EffectsModule} from '@ngrx/effects';
import {SharedModule} from '../shared/shared.module';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {GetTeamRecordsComponent} from './components/get-team-records/get-team-records.component';
import {
  loadByGroundTeamReducer,
  loadByHighestTargetChasedTeamReducer,
  loadByHostTeamReducer,
  loadByInningsExtrasTeamReducer,
  loadByLowestTargetDefendedTeamReducer,
  loadByLowestUnreducedTargetDefendedTeamReducer,
  loadByMatchTeamReducer,
  loadByOppositionTeamReducer,
  loadByResultsTeamReducer,
  loadBySeasonTeamReducer,
  loadBySeriesTeamReducer,
  loadByYearTeamReducer,
  loadInnByInnTeamReducer,
  loadOverallExtrasTeamReducer,
  loadOverallTeamReducer
} from './reducers/record.reducer';
import {RecordEffects} from './effects/record.effects';
import {TeamAggregateRecordUiComponent} from './components/team-aggregate-record-ui/team-aggregate-record-ui.component';
import {TeamOverallComponent} from './components/team-overall/team-overall.component';
import {TeamInningsByInningsUiComponent} from './components/team-by-innings-ui/team-innings-by-innings-ui.component';
import {TeamInnByInnComponent} from './components/team-inn-by-inn/team-inn-by-inn.component';
import {TeamResultsUiComponent} from './components/team-results-ui/team-results-ui.component';
import {TeamResultsComponent} from './components/team-results/team-results.component';
import {SeriesAveragesComponent} from './components/team-series-averages/team-series-averages.component';
import {GroundAveragesComponent} from './components/team-ground-averages/team-ground-averages.component';
import {ByHostComponent} from './components/team-byhost-averages/team-byhost-averages.component';
import {ByOppositionComponent} from './components/team-byopposition/team-byopposition.component';
import {ByYearOfMatchStartComponent} from './components/team-byyearofmatchstart/team-byyearofmatchstart.component';
import {BySeasonComponent} from './components/team-byseason/team-byseason.component';
import {TeamOverallExtrasUiComponent} from './components/team-overallextras-ui/team-overallextras-ui.component';
import {TeamOverallExtrasComponent} from './components/team-overall-extras/team-overall-extras.component';
import {TeamByInningsExtrasUiComponent} from './components/team-byinningsextras-ui/team-byinningsextras-ui.component';
import {TeamByInningsExtrasComponent} from './components/team-byinnings-extras/team-byinnings-extras.component';
import {
  TeamHighestTargetChasedComponent
} from './components/team-highest-target-chased/team-highest-target-chased.component';
import {TeamTargetUiComponent} from './components/team-target-ui/team-target-ui.component';
import {
  TeamLowestTargetDefendedComponent
} from './components/team-lowest-target-defended/team-lowest-target-defended.component';
import {
  TeamLowestUnreducedTargetDefendedComponent
} from './components/team-lowest-unreduced-target-defended/team-lowest-unreduced-target-defended.component';
import {MatchTotalsComponent} from './components/team-match-totals/team-match-totals.component';
import {RadioButton} from "primeng/radiobutton";


const routes: Routes = [
  {path: 'records/teamrecords', component: GetTeamRecordsComponent},
  {path: 'records/teamrecords/overall', component: TeamOverallComponent},
  {path: 'records/teamrecords/inningsbyinnings', component: TeamInnByInnComponent},
  {path: 'records/teamrecords/bymatchresults', component: TeamResultsComponent},
  {path: 'records/teamrecords/matchtotals', component: MatchTotalsComponent},
  {path: 'records/teamrecords/seriesaverages', component: SeriesAveragesComponent},
  {path: 'records/teamrecords/groundaverages', component: GroundAveragesComponent},
  {path: 'records/teamrecords/bycountry', component: ByHostComponent},
  {path: 'records/teamrecords/byopposition', component: ByOppositionComponent},
  {path: 'records/teamrecords/byyearofmatchstart', component: ByYearOfMatchStartComponent},
  {path: 'records/teamrecords/byseason', component: BySeasonComponent},
  {path: 'records/teamrecords/byextrasoverall', component: TeamOverallExtrasComponent},
  {path: 'records/teamrecords/byextrasinnings', component: TeamByInningsExtrasComponent},
  {path: 'records/teamrecords/highesttargetchased', component: TeamHighestTargetChasedComponent},
  {path: 'records/teamrecords/lowesttargetdefended', component: TeamLowestTargetDefendedComponent},
  {path: 'records/teamrecords/lowestunreducedtargetdefended', component: TeamLowestUnreducedTargetDefendedComponent},
];


@NgModule({
  exports: [RouterModule],
  declarations: [
    TeamAggregateRecordUiComponent,
    TeamInningsByInningsUiComponent,
    TeamResultsUiComponent,
    TeamOverallExtrasUiComponent,
    TeamTargetUiComponent,
    TeamByInningsExtrasUiComponent,
    GetTeamRecordsComponent,
    TeamOverallComponent,
    TeamOverallExtrasComponent,
    TeamInnByInnComponent,
    TeamResultsComponent,
    SeriesAveragesComponent,
    GroundAveragesComponent,
    ByHostComponent,
    ByOppositionComponent,
    ByYearOfMatchStartComponent,
    BySeasonComponent,
    TeamByInningsExtrasComponent,
    TeamHighestTargetChasedComponent,
    TeamLowestTargetDefendedComponent,
    TeamLowestUnreducedTargetDefendedComponent,
    MatchTotalsComponent
  ], imports: [SharedModule,
    CommonModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
    StoreModule.forFeature('teamrecords', {
      overall: loadOverallTeamReducer,
      inningsByInnings: loadInnByInnTeamReducer,
      byMatch: loadByMatchTeamReducer,
      byResults: loadByResultsTeamReducer,
      bySeries: loadBySeriesTeamReducer,
      byGround: loadByGroundTeamReducer,
      byHost: loadByHostTeamReducer,
      byOpposition: loadByOppositionTeamReducer,
      byYear: loadByYearTeamReducer,
      bySeason: loadBySeasonTeamReducer,
      overallExtras: loadOverallExtrasTeamReducer,
      byInningsExtras: loadByInningsExtrasTeamReducer,
      byHighestTargetChased: loadByHighestTargetChasedTeamReducer,
      byLowestTargetDefended: loadByLowestTargetDefendedTeamReducer,
      byLowestUnreducedTargetDefended: loadByLowestUnreducedTargetDefendedTeamReducer,
    }),
    EffectsModule.forFeature([RecordEffects]),
    FontAwesomeModule,
    RadioButton
  ],  providers: []
})
export class TeamRecordsModule {
}
