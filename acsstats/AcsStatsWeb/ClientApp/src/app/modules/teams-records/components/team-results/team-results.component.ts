import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {TeamState} from '../../models/app-state';
import {LoadByResultsTeamRecordsAction} from '../../actions/records.actions';
import {FindRecords} from '../../../../models/find-records.model';
import {Observable} from 'rxjs';
import {ResultsUiModel} from '../../models/team-overall-ui.model';

@Component({
    selector: 'app-team-results',
    templateUrl: './team-results.component.html',
    styleUrls: ['./team-results.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class TeamResultsComponent implements OnInit {

  teamUIModel$!: Observable<ResultsUiModel>;

  constructor(
    private teamStore: Store<TeamState>) {
  }

  onDispatch($event: { payload: FindRecords }) {
    this.teamStore.dispatch(LoadByResultsTeamRecordsAction($event))
  }

  ngOnInit(): void {
    this.teamUIModel$ = this.teamStore.select(s => {
        return s.teamrecords.byResults
      }
    )
  }
}
