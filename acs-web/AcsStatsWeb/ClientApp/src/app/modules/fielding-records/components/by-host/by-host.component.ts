import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {FieldingOverallUiModel} from '../../models/fielding-overall-ui.model';
import {Store} from '@ngrx/store';
import {FieldingOverallState} from '../../models/app-state';
import {FindRecords} from '../../../../models/find-records.model';
import {LoadByHostFieldingRecordsAction} from '../../actions/records.actions';

@Component({
    selector: 'app-by-host',
    templateUrl: './by-host.component.html',
    styleUrls: ['./by-host.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class ByHostComponent implements OnInit {


  fieldingUIModel$!: Observable<FieldingOverallUiModel>;

  constructor(private fieldingStore: Store<FieldingOverallState>) {
  }

  ngOnInit(): void {
    this.fieldingUIModel$ = this.fieldingStore.select(s => {
        return s.fieldingrecords.byHost
      }
    )
  }


  onDispatch($event: { payload: FindRecords }) {
    this.fieldingStore.dispatch(LoadByHostFieldingRecordsAction($event))
  }

}
