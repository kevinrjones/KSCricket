import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {InningsByInningsUiModel} from '../../models/team-overall-ui.model';
import {Observable} from 'rxjs';
import {TeamState} from '../../models/app-state';
import {LoadByMatchTeamRecordsAction} from '../../actions/records.actions';
import {FindRecords} from '../../../../models/find-records.model';

@Component({
    selector: 'app-team-inn-by-inn',
    templateUrl: './team-match-totals.component.html',
    styleUrls: ['./team-match-totals.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class MatchTotalsComponent implements OnInit {

  teamInnByInn$!: Observable<InningsByInningsUiModel>;

  constructor(private teamStore: Store<TeamState>) {
  }

  ngOnInit(): void {
    this.teamInnByInn$ = this.teamStore.select(s => {
        return s.teamrecords.byMatch
      }
    )
  }

  dispatch(findRecordsPayload: { payload: FindRecords }) {
    this.teamStore.dispatch(LoadByMatchTeamRecordsAction(findRecordsPayload))

  }
}
