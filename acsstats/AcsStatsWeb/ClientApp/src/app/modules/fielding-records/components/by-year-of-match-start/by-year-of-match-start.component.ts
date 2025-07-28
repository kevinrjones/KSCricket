import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {FieldingOverallUiModel} from '../../models/fielding-overall-ui.model';
import {Store} from '@ngrx/store';
import {FieldingOverallState} from '../../models/app-state';
import {FindRecords} from '../../../../models/find-records.model';
import {LoadByYearFieldingRecordsAction} from '../../actions/records.actions';

@Component({
    selector: 'app-by-year-of-match-start',
    templateUrl: './by-year-of-match-start.component.html',
    styleUrls: ['./by-year-of-match-start.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class ByYearOfMatchStartComponent implements OnInit {

  fieldingUIModel$!: Observable<FieldingOverallUiModel>;

  constructor(private fieldingStore: Store<FieldingOverallState>) {
  }

  ngOnInit(): void {
    this.fieldingUIModel$ = this.fieldingStore.select(s => {
        return s.fieldingrecords.byYear
      }
    )
  }

  onDispatch($event: { payload: FindRecords }) {
    this.fieldingStore.dispatch(LoadByYearFieldingRecordsAction($event))
  }

}
