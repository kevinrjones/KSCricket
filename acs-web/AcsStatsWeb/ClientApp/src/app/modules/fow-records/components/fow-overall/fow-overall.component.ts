import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {FowOverallState} from '../../models/app-state';
import {LoadOverallFowRecordsAction} from '../../actions/records.actions';
import {FindRecords} from '../../../../models/find-records.model';
import {Observable} from 'rxjs';
import {FowOverallUiModel} from '../../models/fow-overall-ui.model';

@Component({
    selector: 'app-fow-overall',
    templateUrl: './fow-overall.component.html',
    styleUrls: ['./fow-overall.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class FowOverallComponent implements OnInit {

  fowUIModel$!: Observable<FowOverallUiModel>;

  constructor(
    private fowStore: Store<FowOverallState>) {
  }


  onDispatch($event: { payload: FindRecords }) {
    this.fowStore.dispatch(LoadOverallFowRecordsAction($event))
  }

  ngOnInit(): void {
    this.fowUIModel$ = this.fowStore.select(s => {
        return s.fowrecords.overall
      }
    )
  }

}
