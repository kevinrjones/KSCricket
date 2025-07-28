import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {BattingOverallUiModel} from '../../models/batting-overall-ui.model';
import {Store} from '@ngrx/store';
import {BattingOverallState} from '../../models/app-state';
import {FindRecords} from '../../../../models/find-records.model';
import {LoadByGroundBattingRecordsAction} from '../../actions/records.actions';

@Component({
    selector: 'app-ground-averages',
    templateUrl: './ground-averages.component.html',
    styleUrls: ['./ground-averages.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class GroundAveragesComponent implements OnInit {

  battingUIModel$!: Observable<BattingOverallUiModel>;

  constructor(
    private battingStore: Store<BattingOverallState>) {
  }

  ngOnInit(): void {
    this.battingUIModel$ = this.battingStore.select(s => {
        return s.battingrecords.byGround
      }
    )
  }

  onDispatch($event: { payload: FindRecords }) {
    this.battingStore.dispatch(LoadByGroundBattingRecordsAction($event))
  }

}
