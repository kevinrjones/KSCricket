import {createReducer, on} from '@ngrx/store';
import {SaveRecordsFormAction} from '../actions/form-state.actions';


export const initialFormState = {matchType: ''};
export const loadSearchFormStateReducer = createReducer(
  initialFormState,
  on(SaveRecordsFormAction, (state, records) => {
    return records.payload;
  })
);
