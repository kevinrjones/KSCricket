import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {FowOverallUiModel} from '../../models/fow-overall-ui.model';
import {Store} from '@ngrx/store';
import {FowOverallState} from '../../models/app-state';
import {FindRecords} from '../../../../models/find-records.model';
import {LoadBySeriesFowRecordsAction} from '../../actions/records.actions';

@Component({
    selector: 'app-series-averages',
    templateUrl: './series-averages.component.html',
    styleUrls: ['./series-averages.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class SeriesAveragesComponent implements OnInit {

  fowUIModel$!: Observable<FowOverallUiModel>;

  constructor(
    private fowStore: Store<FowOverallState>) {
  }

  ngOnInit(): void {
    this.fowUIModel$ = this.fowStore.select(s => {
        return s.fowrecords.bySeries
      }
    )
  }

  onDispatch($event: { payload: FindRecords }) {
    this.fowStore.dispatch(LoadBySeriesFowRecordsAction($event))
  }


}
