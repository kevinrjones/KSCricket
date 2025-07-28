import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from './models/app-state';
import {ErrorDetails} from './models/error.model';
import {RaiseErrorAction} from './actions/error.actions';
import {Observable} from 'rxjs';
import {ErrorLookupService} from './services/error-lookup.service';
import {LoadingService} from './services/loading.service';
import {Timestamp} from './timestamp/Timestamp';
import {DateTime} from 'luxon';
import { MessageService } from 'primeng/api';


@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
    standalone: false
})
export class AppComponent implements OnInit {
  title = 'ACS Cricket Records';
  loading: boolean = false;
  stamp: string;
  private errorState$: Observable<ErrorDetails>;
  private loadingState$: Observable<boolean>;

  constructor(private appStore: Store<AppState>,
              private messageService: MessageService,
              private errorLookupService: ErrorLookupService,
              private _loading: LoadingService) {

    this.resetErrorState()

    this.stamp = DateTime.fromISO(Timestamp.stamp).toLocaleString(DateTime.DATETIME_FULL)

    // dt = DateTime.fromISO(date)
    // return dt.toLocaleString(DateTime.DATE_FULL)

    this.errorState$ = this.appStore.select(s => s.errorState);
    this.loadingState$ = this.appStore.select(s => s.loading)
  }

  ngOnInit(): void {
    this.errorState$.subscribe(s => {
        var message: string
        if (s.id != 0) {
          if (s.message != null) {
            message = this.errorLookupService.getErrorForCode(s.id) + ' - ' + s.message
          }
          else {
            message = this.errorLookupService.getErrorForCode(s.id)
          }
          this.messageService.add({ severity: 'error', summary: 'Error', detail: message, life: 3000 });

          this.resetErrorState()
        }
      }
    );


    this.loadingState$.subscribe(state => {
      this.loading = state;
    });
  }

  resetErrorState() {
    this.appStore.dispatch(RaiseErrorAction({payload: {id: 0, message: null}}))
  }
}
