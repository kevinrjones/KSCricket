import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {InningsByInningsUiModel} from '../../models/batting-overall-ui.model';
import {LoadByMatchBattingRecordsAction} from '../../actions/records.actions';
import {Store} from '@ngrx/store';
import {BattingOverallState} from '../../models/app-state';
import {Observable} from 'rxjs';
import {FindRecords} from '../../../../models/find-records.model';

@Component({
    selector: 'app-match-totals',
    templateUrl: './match-totals.component.html',
    styleUrls: ['./match-totals.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class MatchTotalsComponent implements OnInit {

  battingInnByInn$!: Observable<InningsByInningsUiModel>;

  constructor(private battingStore: Store<BattingOverallState>) {
  }

  ngOnInit(): void {
    this.battingInnByInn$ = this.battingStore.select(s => {
        return s.battingrecords.byMatch
      }
    )
  }


  dispatch(findRecordsPayload: { payload: FindRecords }) {
    this.battingStore.dispatch(LoadByMatchBattingRecordsAction(findRecordsPayload))

  }

}
