import {createReducer, on} from '@ngrx/store';
import {RaiseErrorAction} from '../actions/error.actions';
import {ErrorDetails} from '../models/error.model';


export const initialErrorState: ErrorDetails = {id: 0, message: null};
export const setErrorStateReducer = createReducer(
  initialErrorState,
  on(RaiseErrorAction, (state, records) => {
    return records.payload;
  })
);
