import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {FieldingOverallState} from '../../models/app-state';
import {LoadOverallFieldingRecordsAction} from '../../actions/records.actions';
import {FindRecords} from '../../../../models/find-records.model';
import {Observable} from 'rxjs';
import {FieldingOverallUiModel} from '../../models/fielding-overall-ui.model';

@Component({
    selector: 'app-fielding-overall',
    templateUrl: './fielding-overall.component.html',
    styleUrls: ['./fielding-overall.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class FieldingOverallComponent implements OnInit {

  fieldingUIModel$!: Observable<FieldingOverallUiModel>;

  constructor(
    private fieldingStore: Store<FieldingOverallState>) {
  }


  onDispatch($event: { payload: FindRecords }) {
    this.fieldingStore.dispatch(LoadOverallFieldingRecordsAction($event))
  }

  ngOnInit(): void {
    this.fieldingUIModel$ = this.fieldingStore.select(s => {
        return s.fieldingrecords.overall
      }
    )
  }

}
