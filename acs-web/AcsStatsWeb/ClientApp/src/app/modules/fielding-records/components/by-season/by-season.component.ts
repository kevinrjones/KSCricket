import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {FieldingOverallUiModel} from '../../models/fielding-overall-ui.model';
import {Store} from '@ngrx/store';
import {FieldingOverallState} from '../../models/app-state';
import {FindRecords} from '../../../../models/find-records.model';
import {LoadBySeasonFieldingRecordsAction} from '../../actions/records.actions';

@Component({
    selector: 'app-by-season',
    templateUrl: './by-season.component.html',
    styleUrls: ['./by-season.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class BySeasonComponent implements OnInit {

  fieldingUIModel$!: Observable<FieldingOverallUiModel>;

  constructor(private fieldingStore: Store<FieldingOverallState>) {
  }

  ngOnInit(): void {
    this.fieldingUIModel$ = this.fieldingStore.select(s => {
        return s.fieldingrecords.bySeason
      }
    )
  }

  onDispatch($event: { payload: FindRecords }) {
    this.fieldingStore.dispatch(LoadBySeasonFieldingRecordsAction($event))
  }

}
