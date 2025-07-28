import {createReducer, on} from '@ngrx/store';
import {MatchSubTypeModel} from '../models/match-sub-type.model';
import {LoadMatchSubTypesSuccessAction} from '../actions/match-sub-types.actions';


export const initialByMatchTypeState: MatchSubTypeModel[] = [];
export const matchSubTypeReducer = createReducer(
  initialByMatchTypeState,
  on(LoadMatchSubTypesSuccessAction, (state, matchSubTypes) => {
    return matchSubTypes.payload
  })
);

