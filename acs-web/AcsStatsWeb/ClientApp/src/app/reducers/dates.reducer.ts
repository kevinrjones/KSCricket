import {createReducer, on} from '@ngrx/store';
import {LoadMatchDatesSuccessAction, LoadSeriesDatesSuccessAction} from '../actions/dates.actions';
import {MatchDate} from '../models/date.model';
import {DateTime} from 'luxon';


export const initialSeriesDatesByMatchTypeState: string[] = [];
export const seriesDatesReducer = createReducer(
  initialSeriesDatesByMatchTypeState,
  on(LoadSeriesDatesSuccessAction, (state, dates) => {
    return dates.payload
  })
);

export const initialMatchDatesByMatchTypeState: MatchDate[] = [];
export const matchDatesReducer = createReducer(
  initialMatchDatesByMatchTypeState,
  on(LoadMatchDatesSuccessAction, (state, newState) => {
    let dates: MatchDate[] = newState.payload
    return dates.map(it => {
      // the date format returned from the KTOR app is yyyy-MM_dd, the dotNet app is dd/MM/yyyy
      return {
        date: DateTime.fromFormat(it.date, 'yyyy-MM-dd').toISODate()!,
        dateOffset: it.dateOffset,
        matchType: it.matchType
      }
    })
  })
);

