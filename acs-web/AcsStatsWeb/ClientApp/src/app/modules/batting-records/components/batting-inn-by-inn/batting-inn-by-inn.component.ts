import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {InningsByInningsUiModel} from '../../models/batting-overall-ui.model';
import {Observable} from 'rxjs';
import {BattingOverallState} from '../../models/app-state';
import {LoadInnByInnBattingRecordsAction} from '../../actions/records.actions';
import {FindRecords} from '../../../../models/find-records.model';

@Component({
    selector: 'app-batting-inn-by-inn',
    templateUrl: './batting-inn-by-inn.component.html',
    styleUrls: ['./batting-inn-by-inn.component.css'],
    standalone: false
})
export class BattingInnByInnComponent implements OnInit {

  battingInnByInn$!: Observable<InningsByInningsUiModel>;

  constructor(private battingStore: Store<BattingOverallState>) {
  }

  ngOnInit(): void {
    this.battingInnByInn$ = this.battingStore.select(s => {
        return s.battingrecords.inningsByInnings
      }
    )
  }


  dispatch(findRecordsPayload: { payload: FindRecords }) {
    this.battingStore.dispatch(LoadInnByInnBattingRecordsAction(findRecordsPayload))

  }
}
