import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {BowlingOverallUiModel} from '../../models/bowling-overall-ui.model';
import {Store} from '@ngrx/store';
import {BowlingOverallState} from '../../models/app-state';
import {LoadBySeriesBowlingRecordsAction} from '../../actions/records.actions';
import {FindRecords} from '../../../../models/find-records.model';

@Component({
    selector: 'app-series-averages',
    templateUrl: './series-averages.component.html',
    styleUrls: ['./series-averages.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class SeriesAveragesComponent implements OnInit {

  bowlingOverallUiModel$!: Observable<BowlingOverallUiModel>;

  constructor(private bowlingStore: Store<BowlingOverallState>) {
  }

  ngOnDestroy() {
  }

  ngOnInit(): void {

    this.bowlingOverallUiModel$ = this.bowlingStore.select(s => {
        return s.bowlingrecords.bySeries
      }
    )

  }

  onDispatch(findRecordPayload: { payload: FindRecords }) {
    this.bowlingStore.dispatch(LoadBySeriesBowlingRecordsAction(findRecordPayload))
  }

}
