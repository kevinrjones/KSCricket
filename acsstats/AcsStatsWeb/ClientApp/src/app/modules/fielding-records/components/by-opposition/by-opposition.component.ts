import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {FieldingOverallUiModel} from '../../models/fielding-overall-ui.model';
import {Store} from '@ngrx/store';
import {FieldingOverallState} from '../../models/app-state';
import {FindRecords} from '../../../../models/find-records.model';
import {LoadByOppositionFieldingRecordsAction} from '../../actions/records.actions';

@Component({
    selector: 'app-by-opposition',
    templateUrl: './by-opposition.component.html',
    styleUrls: ['./by-opposition.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class ByOppositionComponent implements OnInit {

  fieldingUIModel$!: Observable<FieldingOverallUiModel>;

  constructor(private fieldingStore: Store<FieldingOverallState>) {
  }

  ngOnInit(): void {
    this.fieldingUIModel$ = this.fieldingStore.select(s => {
        return s.fieldingrecords.byOpposition
      }
    )
  }

  onDispatch($event: { payload: FindRecords }) {
    this.fieldingStore.dispatch(LoadByOppositionFieldingRecordsAction($event))
  }

}
