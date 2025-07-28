import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {FowOverallUiModel} from '../../models/fow-overall-ui.model';
import {Store} from '@ngrx/store';
import {FowOverallState} from '../../models/app-state';
import {FindRecords} from '../../../../models/find-records.model';
import {LoadByGroundFowRecordsAction} from '../../actions/records.actions';

@Component({
    selector: 'app-ground-averages',
    templateUrl: './ground-averages.component.html',
    styleUrls: ['./ground-averages.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class GroundAveragesComponent implements OnInit {

  fowUIModel$!: Observable<FowOverallUiModel>;

  constructor(
    private fowStore: Store<FowOverallState>) {
  }

  ngOnInit(): void {
    this.fowUIModel$ = this.fowStore.select(s => {
        return s.fowrecords.byGround
      }
    )
  }

  onDispatch($event: { payload: FindRecords }) {
    this.fowStore.dispatch(LoadByGroundFowRecordsAction($event))
  }

}
