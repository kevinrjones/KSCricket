import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {FieldingOverallUiModel} from '../../models/fielding-overall-ui.model';
import {Store} from '@ngrx/store';
import {FieldingOverallState} from '../../models/app-state';
import {FindRecords} from '../../../../models/find-records.model';
import {LoadBySeriesFieldingRecordsAction} from '../../actions/records.actions';

@Component({
    selector: 'app-series-averages',
    templateUrl: './series-averages.component.html',
    styleUrls: ['./series-averages.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class SeriesAveragesComponent implements OnInit {

  fieldingUIModel$!: Observable<FieldingOverallUiModel>;

  constructor(
    private fieldingStore: Store<FieldingOverallState>) {
  }

  ngOnInit(): void {
    this.fieldingUIModel$ = this.fieldingStore.select(s => {
        return s.fieldingrecords.bySeries
      }
    )
  }

  onDispatch($event: { payload: FindRecords }) {
    this.fieldingStore.dispatch(LoadBySeriesFieldingRecordsAction($event))
  }


}
