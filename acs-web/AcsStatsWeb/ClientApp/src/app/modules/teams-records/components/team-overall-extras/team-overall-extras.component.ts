import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {TeamState} from '../../models/app-state';
import {LoadOverallExtrasTeamRecordsAction} from '../../actions/records.actions';
import {FindRecords} from '../../../../models/find-records.model';
import {Observable} from 'rxjs';
import {OverallExtrasUiModel} from '../../models/team-overall-ui.model';

@Component({
    selector: 'app-team-overallextras-overall',
    templateUrl: './team-overall-extras.component.html',
    styleUrls: ['./team-overall-extras.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class TeamOverallExtrasComponent implements OnInit {

  extrasUIModel$!: Observable<OverallExtrasUiModel>;

  constructor(
    private teamStore: Store<TeamState>) {
  }

  onDispatch($event: { payload: FindRecords }) {
    this.teamStore.dispatch(LoadOverallExtrasTeamRecordsAction($event))
  }

  ngOnInit(): void {
    this.extrasUIModel$ = this.teamStore.select(s => {
        return s.teamrecords.overallExtras
      }
    )
  }

}
