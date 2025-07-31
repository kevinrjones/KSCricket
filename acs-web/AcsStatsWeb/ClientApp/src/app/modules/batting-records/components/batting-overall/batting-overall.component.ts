import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {BattingOverallState} from '../../models/app-state';
import {LoadOverallBattingRecordsAction} from '../../actions/records.actions';
import {FindRecords} from '../../../../models/find-records.model';
import {Observable} from 'rxjs';
import {BattingOverallUiModel} from '../../models/batting-overall-ui.model';
import {BattingRecordService} from '../../services/batting-record.service';

@Component({
    selector: 'app-batting-overall',
    templateUrl: './batting-overall.component.html',
    styleUrls: ['./batting-overall.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class BattingOverallComponent implements OnInit {

  battingUIModel$!: Observable<BattingOverallUiModel>;

  constructor(
    private battingStore: Store<BattingOverallState>,
    private battingRecordsSearchService: BattingRecordService) {
  }

  onDispatch($event: { payload: FindRecords }) {
    this.battingStore.dispatch(LoadOverallBattingRecordsAction($event))
  }

  ngOnInit(): void {
    this.battingUIModel$ = this.battingStore.select(s => {
        return s.battingrecords.overall
      }
    )
  }

}
