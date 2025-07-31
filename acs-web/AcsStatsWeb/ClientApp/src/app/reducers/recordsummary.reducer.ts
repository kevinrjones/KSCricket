import {createReducer, on} from '@ngrx/store';
import {LoadRecordSummariesSuccessAction} from '../actions/recordsummary.actions';


export const initialSeriesDatesByMatchTypeState = {};
export const recordSummaryReducer = createReducer(
  initialSeriesDatesByMatchTypeState,
  on(LoadRecordSummariesSuccessAction, (state, summary) => {
    return summary.payload
  })
);


