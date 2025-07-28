import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {TeamState} from '../../models/app-state';
import {LoadByInningsExtrasTeamRecordsAction} from '../../actions/records.actions';
import {FindRecords} from '../../../../models/find-records.model';
import {Observable} from 'rxjs';
import {ByInningsExtrasUiModel} from '../../models/team-overall-ui.model';

@Component({
    selector: 'app-team-byinnings-overall',
    templateUrl: './team-byinnings-extras.component.html',
    styleUrls: ['./team-byinnings-extras.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class TeamByInningsExtrasComponent implements OnInit {

  extrasUIModel$!: Observable<ByInningsExtrasUiModel>;

  constructor(
    private teamStore: Store<TeamState>) {
  }

  onDispatch($event: { payload: FindRecords }) {
    this.teamStore.dispatch(LoadByInningsExtrasTeamRecordsAction($event))
  }

  ngOnInit(): void {
    this.extrasUIModel$ = this.teamStore.select(s => {
        return s.teamrecords.byInningsExtras
      }
    )
  }

}
