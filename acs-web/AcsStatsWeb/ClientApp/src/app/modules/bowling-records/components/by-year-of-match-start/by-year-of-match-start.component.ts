import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {BowlingOverallUiModel} from '../../models/bowling-overall-ui.model';
import {Store} from '@ngrx/store';
import {BowlingOverallState} from '../../models/app-state';
import {LoadByYearBowlingRecordsAction} from '../../actions/records.actions';
import {FindRecords} from '../../../../models/find-records.model';

@Component({
    selector: 'app-by-year-of-match-start',
    templateUrl: './by-year-of-match-start.component.html',
    styleUrls: ['./by-year-of-match-start.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class ByYearOfMatchStartComponent implements OnInit {
  bowlingOverallUiModel$!: Observable<BowlingOverallUiModel>;

  constructor(private bowlingStore: Store<BowlingOverallState>) {
  }

  ngOnDestroy() {
  }

  ngOnInit(): void {

    this.bowlingOverallUiModel$ = this.bowlingStore.select(s => {
        return s.bowlingrecords.byYear
      }
    )

  }

  onDispatch(findRecordPayload: { payload: FindRecords }) {
    this.bowlingStore.dispatch(LoadByYearBowlingRecordsAction(findRecordPayload))
  }
}
