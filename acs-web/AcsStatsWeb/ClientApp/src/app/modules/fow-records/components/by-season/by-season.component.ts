import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {FowOverallUiModel} from '../../models/fow-overall-ui.model';
import {Store} from '@ngrx/store';
import {FowOverallState} from '../../models/app-state';
import {FindRecords} from '../../../../models/find-records.model';
import {LoadBySeasonFowRecordsAction} from '../../actions/records.actions';

@Component({
    selector: 'app-by-season',
    templateUrl: './by-season.component.html',
    styleUrls: ['./by-season.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class BySeasonComponent implements OnInit {

  fowUIModel$!: Observable<FowOverallUiModel>;

  constructor(private fowStore: Store<FowOverallState>) {
  }

  ngOnInit(): void {
    this.fowUIModel$ = this.fowStore.select(s => {
        return s.fowrecords.bySeason
      }
    )
  }

  onDispatch($event: { payload: FindRecords }) {
    this.fowStore.dispatch(LoadBySeasonFowRecordsAction($event))
  }

}
