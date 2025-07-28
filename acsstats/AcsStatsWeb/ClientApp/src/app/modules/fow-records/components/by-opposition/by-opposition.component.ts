import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {FowOverallUiModel} from '../../models/fow-overall-ui.model';
import {Store} from '@ngrx/store';
import {FowOverallState} from '../../models/app-state';
import {FindRecords} from '../../../../models/find-records.model';
import {LoadByOppositionFowRecordsAction} from '../../actions/records.actions';

@Component({
    selector: 'app-by-opposition',
    templateUrl: './by-opposition.component.html',
    styleUrls: ['./by-opposition.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class ByOppositionComponent implements OnInit {

  fowUIModel$!: Observable<FowOverallUiModel>;

  constructor(private fowStore: Store<FowOverallState>) {
  }

  ngOnInit(): void {
    this.fowUIModel$ = this.fowStore.select(s => {
        return s.fowrecords.byOpposition
      }
    )
  }

  onDispatch($event: { payload: FindRecords }) {
    this.fowStore.dispatch(LoadByOppositionFowRecordsAction($event))
  }

}
