import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {TeamState} from '../../models/app-state';
import {FindRecords} from '../../../../models/find-records.model';
import {Observable} from 'rxjs';
import {ByTargetUiModel} from '../../models/team-overall-ui.model';
import {LoadByLowestUnreducedTargetDefendedTeamRecordsAction} from '../../actions/records.actions';

@Component({
    selector: 'app-team-lowest-unreduced-target-defended',
    templateUrl: './team-lowest-unreduced-target-defended.component.html',
    styleUrls: ['./team-lowest-unreduced-target-defended.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class TeamLowestUnreducedTargetDefendedComponent implements OnInit {

  teamUIModel$!: Observable<ByTargetUiModel>;

  constructor(
    private teamStore: Store<TeamState>) {
  }


  onDispatch($event: { payload: FindRecords }) {
    this.teamStore.dispatch(LoadByLowestUnreducedTargetDefendedTeamRecordsAction($event))
  }

  ngOnInit(): void {
    this.teamUIModel$ = this.teamStore.select(s => {
        return s.teamrecords.byLowestUnreducedTargetDefended
      }
    )
  }

}
