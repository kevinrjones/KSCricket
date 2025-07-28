import {createAction, props} from '@ngrx/store';
import {FindScorecardState} from "../models/find-scorecard.models";
import {FindPlayerState} from "../models/find-player.models";


const SAVE_SCORECARD_SEARCH_FORM = 'SAVE_SCORECARD_SEARCH_FORM;';
export const SaveScorecardSearchFormAction = createAction(SAVE_SCORECARD_SEARCH_FORM, props<{
  payload: FindScorecardState
}>())

const SAVE_PLAYER_SEARCH_FORM = 'SAVE_PLAYER_SEARCH_FORM;';
export const SavePlayerSearchFormAction = createAction(SAVE_PLAYER_SEARCH_FORM, props<{ payload: FindPlayerState }>())
