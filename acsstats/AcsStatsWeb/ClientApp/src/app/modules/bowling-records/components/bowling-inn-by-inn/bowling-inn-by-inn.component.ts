import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {InningsByInningsUiModel} from '../../models/bowling-overall-ui.model';
import {Store} from '@ngrx/store';
import {BowlingOverallState} from '../../models/app-state';
import {LoadInnByInnBowlingRecordsAction} from '../../actions/records.actions';
import {FindRecords} from '../../../../models/find-records.model';

@Component({
    selector: 'app-bowling-inn-by-inn',
    templateUrl: './bowling-inn-by-inn.component.html',
    styleUrls: ['./bowling-inn-by-inn.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class BowlingInnByInnComponent implements OnInit {

  bowlingInnByInn$!: Observable<InningsByInningsUiModel>;

  constructor(private bowlingStore: Store<BowlingOverallState>) {
  }

  ngOnDestroy() {
  }

  ngOnInit(): void {

    this.bowlingInnByInn$ = this.bowlingStore.select(s => {
        return s.bowlingrecords.inningsByInnings
      }
    )

  }

  onDispatch(findRecordPayload: { payload: FindRecords }) {
    this.bowlingStore.dispatch(LoadInnByInnBowlingRecordsAction(findRecordPayload))
  }
}
