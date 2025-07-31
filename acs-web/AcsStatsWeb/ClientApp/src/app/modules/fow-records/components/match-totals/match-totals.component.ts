import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {InningsByInningsUiModel} from '../../models/fow-overall-ui.model';
import {LoadByMatchFowRecordsAction} from '../../actions/records.actions';
import {Store} from '@ngrx/store';
import {FowOverallState} from '../../models/app-state';
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

  fowInnByInn$!: Observable<InningsByInningsUiModel>;

  constructor(private fowStore: Store<FowOverallState>) {
  }

  ngOnInit(): void {
    this.fowInnByInn$ = this.fowStore.select(s => {
        return s.fowrecords.byMatch
      }
    )
  }


  dispatch(findRecordsPayload: { payload: FindRecords }) {
    this.fowStore.dispatch(LoadByMatchFowRecordsAction(findRecordsPayload))

  }

}
