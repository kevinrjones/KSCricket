import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {BattingOverallUiModel} from '../../models/batting-overall-ui.model';
import {Store} from '@ngrx/store';
import {BattingOverallState} from '../../models/app-state';
import {FindRecords} from '../../../../models/find-records.model';
import {LoadByYearBattingRecordsAction} from '../../actions/records.actions';

@Component({
    selector: 'app-by-year-of-match-start',
    templateUrl: './by-year-of-match-start.component.html',
    styleUrls: ['./by-year-of-match-start.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class ByYearOfMatchStartComponent implements OnInit {

  battingUIModel$!: Observable<BattingOverallUiModel>;

  constructor(private battingStore: Store<BattingOverallState>) {
  }

  ngOnInit(): void {
    this.battingUIModel$ = this.battingStore.select(s => {
        return s.battingrecords.byYear
      }
    )
  }

  onDispatch($event: { payload: FindRecords }) {
    this.battingStore.dispatch(LoadByYearBattingRecordsAction($event))
  }

}
