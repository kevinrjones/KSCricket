import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {FieldingOverallUiModel} from '../../models/fielding-overall-ui.model';
import {Store} from '@ngrx/store';
import {FieldingOverallState} from '../../models/app-state';
import {FindRecords} from '../../../../models/find-records.model';
import {LoadByGroundFieldingRecordsAction} from '../../actions/records.actions';

@Component({
    selector: 'app-ground-averages',
    templateUrl: './ground-averages.component.html',
    styleUrls: ['./ground-averages.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class GroundAveragesComponent implements OnInit {

  fieldingUIModel$!: Observable<FieldingOverallUiModel>;

  constructor(
    private fieldingStore: Store<FieldingOverallState>) {
  }

  ngOnInit(): void {
    this.fieldingUIModel$ = this.fieldingStore.select(s => {
        return s.fieldingrecords.byGround
      }
    )
  }

  onDispatch($event: { payload: FindRecords }) {
    this.fieldingStore.dispatch(LoadByGroundFieldingRecordsAction($event))
  }

}
