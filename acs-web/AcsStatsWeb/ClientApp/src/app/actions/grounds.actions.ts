import {createAction, props} from '@ngrx/store'
import {Ground} from '../models/ground.model';


export const LOAD_GROUNDS = 'LOAD_GROUNDS;';
export const LOAD_GROUNDS_SUCCESS = 'LOAD_GROUNDS_SUCCESS;';


export const LoadGroundsAction = createAction(LOAD_GROUNDS, props<{
  payload: { matchType: string, countryId: number }
}>())
export const LoadGroundsSuccessAction = createAction(LOAD_GROUNDS_SUCCESS, props<{ payload: Ground[] }>())

