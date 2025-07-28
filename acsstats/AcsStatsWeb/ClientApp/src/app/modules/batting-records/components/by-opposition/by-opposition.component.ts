import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {BattingOverallUiModel} from '../../models/batting-overall-ui.model';
import {Store} from '@ngrx/store';
import {BattingOverallState} from '../../models/app-state';
import {FindRecords} from '../../../../models/find-records.model';
import {LoadByOppositionBattingRecordsAction} from '../../actions/records.actions';

@Component({
    selector: 'app-by-opposition',
    templateUrl: './by-opposition.component.html',
    styleUrls: ['./by-opposition.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class ByOppositionComponent implements OnInit {

  battingUIModel$!: Observable<BattingOverallUiModel>;

  constructor(private battingStore: Store<BattingOverallState>) {
  }

  ngOnInit(): void {
    this.battingUIModel$ = this.battingStore.select(s => {
        return s.battingrecords.byOpposition
      }
    )
  }

  onDispatch($event: { payload: FindRecords }) {
    this.battingStore.dispatch(LoadByOppositionBattingRecordsAction($event))
  }

}
