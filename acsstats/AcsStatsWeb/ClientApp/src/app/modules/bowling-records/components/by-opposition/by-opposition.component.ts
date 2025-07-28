import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {BowlingOverallUiModel} from '../../models/bowling-overall-ui.model';
import {Store} from '@ngrx/store';
import {BowlingOverallState} from '../../models/app-state';
import {LoadByOppositionBowlingRecordsAction} from '../../actions/records.actions';
import {FindRecords} from '../../../../models/find-records.model';


@Component({
    selector: 'app-by-opposition',
    templateUrl: './by-opposition.component.html',
    styleUrls: ['./by-opposition.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class ByOppositionComponent implements OnInit {


  bowlingOverallUiModel$!: Observable<BowlingOverallUiModel>;

  constructor(private bowlingStore: Store<BowlingOverallState>) {
  }

  ngOnDestroy() {
  }

  ngOnInit(): void {

    this.bowlingOverallUiModel$ = this.bowlingStore.select(s => {
        return s.bowlingrecords.byOpposition
      }
    )

  }

  onDispatch(findRecordPayload: { payload: FindRecords }) {
    this.bowlingStore.dispatch(LoadByOppositionBowlingRecordsAction(findRecordPayload))
  }
}
