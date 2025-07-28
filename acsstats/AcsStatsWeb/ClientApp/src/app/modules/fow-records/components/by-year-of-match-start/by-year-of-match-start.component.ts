import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {FowOverallUiModel} from '../../models/fow-overall-ui.model';
import {Store} from '@ngrx/store';
import {FowOverallState} from '../../models/app-state';
import {FindRecords} from '../../../../models/find-records.model';
import {LoadByYearFowRecordsAction} from '../../actions/records.actions';

@Component({
    selector: 'app-by-year-of-match-start',
    templateUrl: './by-year-of-match-start.component.html',
    styleUrls: ['./by-year-of-match-start.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class ByYearOfMatchStartComponent implements OnInit {

  fowUIModel$!: Observable<FowOverallUiModel>;

  constructor(private fowStore: Store<FowOverallState>) {
  }

  ngOnInit(): void {
    this.fowUIModel$ = this.fowStore.select(s => {
        return s.fowrecords.byYear
      }
    )
  }

  onDispatch($event: { payload: FindRecords }) {
    this.fowStore.dispatch(LoadByYearFowRecordsAction($event))
  }

}
