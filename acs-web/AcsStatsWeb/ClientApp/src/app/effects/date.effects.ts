import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, map, mergeMap} from 'rxjs/operators';
import {of} from 'rxjs';
import {
  LoadMatchDatesAction,
  LoadMatchDatesSuccessAction,
  LoadSeriesDatesAction,
  LoadSeriesDatesSuccessAction
} from '../actions/dates.actions';
import {DatesService} from '../services/dates.service';
import {Envelope} from '../models/envelope';
import {MatchDate} from '../models/date.model';
import {createError} from '../helpers/ErrorHelper';
import {RaiseErrorAction} from '../actions/error.actions';

@Injectable()
export class DateEffects {
  // todo: in the map check that the envelope is valid and if not return an error
  loadSeriesDates$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadSeriesDatesAction),
      mergeMap(action => this.datesService.getSeriesDatesForMatchType(action.payload)
        .pipe(
          map(seriesDates => LoadSeriesDatesSuccessAction({payload: seriesDates.result})),
          catchError((err) => of(RaiseErrorAction({payload: createError(1, 'Unable to get dates')})))
        ))
    );
  });
  loadMatchDates$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadMatchDatesAction),
      mergeMap(action => this.datesService.getMatchDatesForMatchType(action.payload)
        .pipe(
          map((matchDates: Envelope<MatchDate[]>) => {
              if (matchDates.errorMessage != null && matchDates.errorMessage != '')
                return RaiseErrorAction({payload: createError(2, 'Unable to get match dates')})

              let dates = matchDates.result
              if (dates.length != 2)
                return RaiseErrorAction({payload: createError(2, 'Invalid match dates')})

              return LoadMatchDatesSuccessAction({payload: dates})
            }
          ),
          catchError((err) => {
              if (err.error)
                return of(RaiseErrorAction({payload: createError(2, err.error.errorMessage)}))
              else
                return of(RaiseErrorAction({payload: createError(1, 'Unable to get match dates')}))
            }
          )
        ))
    );
  });

  constructor(
    private datesService: DatesService
    , private actions$: Actions
  ) {
  }

}
