import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {FowOverallUiModel} from '../../models/fow-overall-ui.model';
import {Store} from '@ngrx/store';
import {FowOverallState} from '../../models/app-state';
import {FindRecords} from '../../../../models/find-records.model';
import {LoadByHostFowRecordsAction} from '../../actions/records.actions';

@Component({
    selector: 'app-by-host',
    templateUrl: './by-host.component.html',
    styleUrls: ['./by-host.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class ByHostComponent implements OnInit {


  fowUIModel$!: Observable<FowOverallUiModel>;

  constructor(private fowStore: Store<FowOverallState>) {
  }

  ngOnInit(): void {
    this.fowUIModel$ = this.fowStore.select(s => {
        return s.fowrecords.byHost
      }
    )
  }


  onDispatch($event: { payload: FindRecords }) {
    this.fowStore.dispatch(LoadByHostFowRecordsAction($event))
  }

}
