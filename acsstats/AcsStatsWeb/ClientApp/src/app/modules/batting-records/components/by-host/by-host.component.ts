import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {BattingOverallUiModel} from '../../models/batting-overall-ui.model';
import {Store} from '@ngrx/store';
import {BattingOverallState} from '../../models/app-state';
import {FindRecords} from '../../../../models/find-records.model';
import {LoadByHostBattingRecordsAction} from '../../actions/records.actions';

@Component({
    selector: 'app-by-host',
    templateUrl: './by-host.component.html',
    styleUrls: ['./by-host.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class ByHostComponent implements OnInit {


  battingUIModel$!: Observable<BattingOverallUiModel>;

  constructor(private battingStore: Store<BattingOverallState>) {
  }

  ngOnInit(): void {
    this.battingUIModel$ = this.battingStore.select(s => {
        return s.battingrecords.byHost
      }
    )
  }


  onDispatch($event: { payload: FindRecords }) {
    this.battingStore.dispatch(LoadByHostBattingRecordsAction($event))
  }

}
