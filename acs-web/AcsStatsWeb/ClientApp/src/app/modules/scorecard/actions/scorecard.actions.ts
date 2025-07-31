import {createAction, props} from '@ngrx/store'
import {FindScorecard} from '../models/scorecard-search.model';
import {ScorecardListItem} from '../models/scorecard-list-item.model';
import {Scorecard} from '../models/scorecard.model';


const LOAD_SCORECARD_LIST = 'LOAD_SCORECARD_LIST;';
const LOAD_SCORECARD_LIST_SUCCESS = 'LOAD_SCORECARD_LIST_SUCCESS;';
const LOAD_SCORECARD_LIST_TOURNAMENT = 'LOAD_SCORECARD_LIST_TOURNAMENT;';

export const LoadScorecardListAction = createAction(LOAD_SCORECARD_LIST, props<{ payload: FindScorecard }>())
export const LoadScorecardTournamentListAction = createAction(LOAD_SCORECARD_LIST_TOURNAMENT, props<{
  payload: string
}>())
export const LoadScorecardListSuccessAction = createAction(LOAD_SCORECARD_LIST_SUCCESS, props<{
  payload: ScorecardListItem[]
}>())


const LOAD_SCORECARD = 'LOAD_SCORECARD;';
const LOAD_SCORECARD_SUCCESS = 'LOAD_SCORECARD_SUCCESS;';

export const LoadScorecardAction = createAction(LOAD_SCORECARD, props<{ payload: string }>())
export const LoadScorecardSuccessAction = createAction(LOAD_SCORECARD_SUCCESS, props<{ payload: Scorecard }>())

const LOAD_SCORECARD_BY_ID = 'LOAD_SCORECARD_BY_ID;';
const LOAD_SCORECARD_BY_ID_SUCCESS = 'LOAD_SCORECARD_SUCCESS_BY_ID;';

export const LoadScorecardByIdAction = createAction(LOAD_SCORECARD_BY_ID, props<{ payload: string }>())

const LOAD_BYDECADE = 'LOAD_BYDECADE;';
const LOAD_BYDECADE_SUCCESS = 'LOAD_BYDECADE_SUCCESS;';

export const LoadByDecadeAction = createAction(LOAD_BYDECADE, props<{ payload: string }>())
export const LoadByDecadeSuccessAction = createAction(LOAD_BYDECADE_SUCCESS, props<{
  payload: { [decade: number]: string[] }
}>())

const LOAD_BYYEAR = 'LOAD_BYYEAR;';
const LOAD_BYYEAR_SUCCESS = 'LOAD_BYYEAR_SUCCESS;';

export const LoadByYearAction = createAction(LOAD_BYYEAR, props<{ payload: { type: string, year: string } }>())
export const LoadByYearSuccessAction = createAction(LOAD_BYYEAR_SUCCESS, props<{ payload: string[] }>())
