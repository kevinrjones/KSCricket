import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {InningsByInningsUiModel} from '../../models/fielding-overall-ui.model';
import {Observable} from 'rxjs';
import {FieldingOverallState} from '../../models/app-state';
import {LoadByMatchFieldingRecordsAction} from '../../actions/records.actions';
import {FindRecords} from '../../../../models/find-records.model';

@Component({
    selector: 'app-match-totals',
    templateUrl: './match-totals.component.html',
    styleUrls: ['./match-totals.component.css'],
    standalone: false
})
export class MatchTotalsComponent implements OnInit {

  fieldingInnByInn$!: Observable<InningsByInningsUiModel>;

  constructor(private fieldingStore: Store<FieldingOverallState>) {
  }

  ngOnInit(): void {
    this.fieldingInnByInn$ = this.fieldingStore.select(s => {
        return s.fieldingrecords.byMatch
      }
    )
  }


  dispatch(findRecordsPayload: { payload: FindRecords }) {
    this.fieldingStore.dispatch(LoadByMatchFieldingRecordsAction(findRecordsPayload))

  }
}
