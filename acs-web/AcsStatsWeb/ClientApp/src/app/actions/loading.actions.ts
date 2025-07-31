import {createAction, props} from '@ngrx/store';


export const SET_LOADING = 'SET_LOADING;';


export const SetLoadingAction = createAction(SET_LOADING, props<{ payload: boolean }>())
