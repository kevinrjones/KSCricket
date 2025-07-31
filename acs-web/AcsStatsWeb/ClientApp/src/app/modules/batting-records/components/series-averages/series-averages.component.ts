import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {BattingOverallUiModel} from '../../models/batting-overall-ui.model';
import {Store} from '@ngrx/store';
import {BattingOverallState} from '../../models/app-state';
import {FindRecords} from '../../../../models/find-records.model';
import {LoadBySeriesBattingRecordsAction} from '../../actions/records.actions';

@Component({
    selector: 'app-series-averages',
    templateUrl: './series-averages.component.html',
    styleUrls: ['./series-averages.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class SeriesAveragesComponent implements OnInit {

  battingUIModel$!: Observable<BattingOverallUiModel>;

  constructor(
    private battingStore: Store<BattingOverallState>) {
  }

  ngOnInit(): void {
    this.battingUIModel$ = this.battingStore.select(s => {
        return s.battingrecords.bySeries
      }
    )
  }

  onDispatch($event: { payload: FindRecords }) {
    this.battingStore.dispatch(LoadBySeriesBattingRecordsAction($event))
  }


}
