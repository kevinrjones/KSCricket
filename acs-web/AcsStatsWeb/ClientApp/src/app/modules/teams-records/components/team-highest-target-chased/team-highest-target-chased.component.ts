import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {TeamState} from '../../models/app-state';
import {FindRecords} from '../../../../models/find-records.model';
import {Observable} from 'rxjs';
import {ByTargetUiModel} from '../../models/team-overall-ui.model';
import {LoadByHighestTargetChasedTeamRecordsAction} from '../../actions/records.actions';

@Component({
    selector: 'app-team-highest-target-chased',
    templateUrl: './team-highest-target-chased.component.html',
    styleUrls: ['./team-highest-target-chased.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class TeamHighestTargetChasedComponent implements OnInit {

  teamUIModel$!: Observable<ByTargetUiModel>;

  constructor(
    private teamStore: Store<TeamState>) {
  }


  onDispatch($event: { payload: FindRecords }) {
    this.teamStore.dispatch(LoadByHighestTargetChasedTeamRecordsAction($event))
  }

  ngOnInit(): void {
    this.teamUIModel$ = this.teamStore.select(s => {
        return s.teamrecords.byHighestTargetChased
      }
    )
  }

}
