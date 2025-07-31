import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {TeamState} from '../../models/app-state';
import {LoadOverallTeamRecordsAction} from '../../actions/records.actions';
import {FindRecords} from '../../../../models/find-records.model';
import {Observable} from 'rxjs';
import {TeamOverallUiModel} from '../../models/team-overall-ui.model';

@Component({
    selector: 'app-team-overall',
    templateUrl: './team-overall.component.html',
    styleUrls: ['./team-overall.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class TeamOverallComponent implements OnInit {

  teamUIModel$!: Observable<TeamOverallUiModel>;

  constructor(
    private teamStore: Store<TeamState>) {
  }


  onDispatch($event: { payload: FindRecords }) {
    this.teamStore.dispatch(LoadOverallTeamRecordsAction($event))
  }

  ngOnInit(): void {
    this.teamUIModel$ = this.teamStore.select(s => {
        return s.teamrecords.overall
      }
    )
  }

}
