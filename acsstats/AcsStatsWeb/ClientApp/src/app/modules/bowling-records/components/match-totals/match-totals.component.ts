import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {InningsByInningsUiModel} from '../../models/bowling-overall-ui.model';
import {Store} from '@ngrx/store';
import {BowlingOverallState} from '../../models/app-state';
import {LoadByMatchBowlingRecordsAction} from '../../actions/records.actions';
import {FindRecords} from '../../../../models/find-records.model';

@Component({
    selector: 'app-match-totals',
    templateUrl: './match-totals.component.html',
    styleUrls: ['./match-totals.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class MatchTotalsComponent implements OnInit {


  bowlingInnByInn$!: Observable<InningsByInningsUiModel>;

  constructor(private bowlingStore: Store<BowlingOverallState>) {
  }

  ngOnDestroy() {
  }

  ngOnInit(): void {

    this.bowlingInnByInn$ = this.bowlingStore.select(s => {
        return s.bowlingrecords.byMatch
      }
    )

  }

  onDispatch(findRecordPayload: { payload: FindRecords }) {
    this.bowlingStore.dispatch(LoadByMatchBowlingRecordsAction(findRecordPayload))
  }

}

