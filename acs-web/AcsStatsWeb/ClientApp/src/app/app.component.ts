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
import {MessageService} from 'primeng/api';
import {DatabaseDateService} from "./services/databasedate.service";
import {Envelope} from "./models/envelope";
import {DateHelperService} from "./modules/shared/services/dateHelperService";
import {log} from "@angular-devkit/build-angular/src/builders/ssr-dev-server";


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
  public dateOfLastChange: string = "";
  private dateOfLastChange$: Observable<Envelope<string>>;
  private errorState$: Observable<ErrorDetails>;
  private loadingState$: Observable<boolean>;

  constructor(private appStore: Store<AppState>,
              private messageService: MessageService,
              private errorLookupService: ErrorLookupService,
              private databaseDateService: DatabaseDateService,
              private dateHelperService: DateHelperService,) {

    this.resetErrorState()

    this.stamp = dateHelperService.asDate(Timestamp.stamp, '', DateTime.DATETIME_FULL)

    this.errorState$ = this.appStore.select(s => s.errorState);
    this.loadingState$ = this.appStore.select(s => s.loading)
    this.dateOfLastChange$ = this.databaseDateService.getDateOfLastAddedMatch()
  }

  ngOnInit(): void {
    this.errorState$.subscribe(s => {
        var message: string
        if (s.id != 0) {
          if (s.message != null) {
            message = this.errorLookupService.getErrorForCode(s.id) + ' - ' + s.message
          } else {
            message = this.errorLookupService.getErrorForCode(s.id)
          }
          this.messageService.add({severity: 'error', summary: 'Error', detail: message, life: 3000});

          this.resetErrorState()
        }
      }
    );


    this.loadingState$.subscribe(state => {
      this.loading = state;
    });

    this.dateOfLastChange$.subscribe(date => {
      this.dateOfLastChange = this.dateHelperService.asDate(date.result, 'unknown', DateTime.DATETIME_FULL)
    })
  }

  resetErrorState() {
    this.appStore.dispatch(RaiseErrorAction({payload: {id: 0, message: null}}))
  }
}
