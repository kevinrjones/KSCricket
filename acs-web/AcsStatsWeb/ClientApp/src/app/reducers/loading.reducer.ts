import {createReducer, on} from '@ngrx/store';
import {SetLoadingAction} from '../actions/loading.actions';


export const loadingStateReducer = createReducer(
  false,
  on(SetLoadingAction, (state, loading) => {
    return loading.payload
  })
);

