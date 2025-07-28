import {createAction, props} from '@ngrx/store'
import {MatchSubTypeModel} from '../models/match-sub-type.model';

export const LOAD_MATCHSUBTYPES = 'LOAD_MATCHSUBTYPES;';
export const LOAD_MATCHSUBTYPES_SUCCESS = 'LOAD_MATCHSUBTYPES_SUCCESS;';

export const LoadMatchSubTypesAction = createAction(LOAD_MATCHSUBTYPES, props<{ payload: string }>())
export const LoadMatchSubTypesSuccessAction = createAction(LOAD_MATCHSUBTYPES_SUCCESS, props<{
  payload: MatchSubTypeModel[]
}>())


