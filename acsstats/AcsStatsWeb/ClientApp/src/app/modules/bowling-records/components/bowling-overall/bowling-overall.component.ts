import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {BowlingOverallState} from '../../models/app-state';
import {LoadOverallBowlingRecordsAction} from '../../actions/records.actions';
import {Observable} from 'rxjs';
import {BowlingOverallUiModel} from '../../models/bowling-overall-ui.model';
import {FindRecords} from '../../../../models/find-records.model';

@Component({
    selector: 'app-bowling-overall',
    templateUrl: './bowling-overall.component.html',
    styleUrls: ['./bowling-overall.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class BowlingOverallComponent implements OnInit {
  bowlingOverallUiModel$!: Observable<BowlingOverallUiModel>;

  constructor(private bowlingStore: Store<BowlingOverallState>) {
  }

  ngOnDestroy() {
  }

  ngOnInit(): void {

    this.bowlingOverallUiModel$ = this.bowlingStore.select(s => {
        return s.bowlingrecords.overall
      }
    )

  }

  onDispatch(findRecordPayload: { payload: FindRecords }) {
    this.bowlingStore.dispatch(LoadOverallBowlingRecordsAction(findRecordPayload))
  }
}
