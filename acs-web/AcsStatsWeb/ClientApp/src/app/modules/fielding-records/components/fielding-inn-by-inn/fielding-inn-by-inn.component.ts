import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {InningsByInningsUiModel} from '../../models/fielding-overall-ui.model';
import {Observable} from 'rxjs';
import {FieldingOverallState} from '../../models/app-state';
import {LoadInnByInnFieldingRecordsAction} from '../../actions/records.actions';
import {FindRecords} from '../../../../models/find-records.model';

@Component({
    selector: 'app-fielding-inn-by-inn',
    templateUrl: './fielding-inn-by-inn.component.html',
    styleUrls: ['./fielding-inn-by-inn.component.css'],
    standalone: false
})
export class FieldingInnByInnComponent implements OnInit {

  fieldingInnByInn$!: Observable<InningsByInningsUiModel>;

  constructor(private fieldingStore: Store<FieldingOverallState>) {
  }

  ngOnInit(): void {
    this.fieldingInnByInn$ = this.fieldingStore.select(s => {
        return s.fieldingrecords.inningsByInnings
      }
    )
  }


  dispatch(findRecordsPayload: { payload: FindRecords }) {
    this.fieldingStore.dispatch(LoadInnByInnFieldingRecordsAction(findRecordsPayload))

  }
}
