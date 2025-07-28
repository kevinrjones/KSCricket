import {createAction, props} from '@ngrx/store'
import {PlayerListUiModel} from '../models/player';
import {FindPlayer} from '../models/find-player.model';
import {PlayerBattingDetails, PlayerBiography, PlayerBowlingDetails, PlayerOverall} from '../playerbiography.model';


const LOAD_PLAYERS = 'LOAD_PLAYERS;';
const LOAD_PLAYERS_SUCCESS = 'LOAD_PLAYERS_SUCCESS;';

export const LoadPlayersAction = createAction(LOAD_PLAYERS, props<{ payload: FindPlayer }>())
export const LoadPlayersSuccessAction = createAction(LOAD_PLAYERS_SUCCESS, props<{ payload: PlayerListUiModel }>())

const LOAD_PLAYER_BIOGRAPHY = 'LOAD_PLAYER;';
const LOAD_PLAYER_BIOGRAPHY_SUCCESS = 'LOAD_PLAYER_BIOGRAPHY_SUCCESS;';

export const LoadPlayerBiographyAction = createAction(LOAD_PLAYER_BIOGRAPHY, props<{ payload: number }>())
export const LoadPlayerBiographySuccessAction = createAction(LOAD_PLAYER_BIOGRAPHY_SUCCESS, props<{
  payload: PlayerBiography
}>())

const LOAD_PLAYER_OVERALL = 'LOAD_PLAYER_OVERAL;';
const LOAD_PLAYER_OVERALL_SUCCESS = 'LOAD_PLAYER_OVERALL_SUCCESS;';

export const LoadPlayerOverallAction = createAction(LOAD_PLAYER_OVERALL, props<{ payload: number }>())
export const LoadPlayerOverallSuccessAction = createAction(LOAD_PLAYER_OVERALL_SUCCESS, props<{
  payload: Array<PlayerOverall[]>
}>())

const LOAD_PLAYER_BATTING_DETAILS = 'LOAD_PLAYER_BATTING_DETAILS;';
const LOAD_PLAYER_BATTING_DETAILS_SUCCESS = 'LOAD_PLAYER_BATTING_DETAILS_SUCCESS;';

export const LoadPlayerBattingDetailsAction = createAction(LOAD_PLAYER_BATTING_DETAILS, props<{ payload: number }>())
export const LoadPlayerBattingDetailsSuccessAction = createAction(LOAD_PLAYER_BATTING_DETAILS_SUCCESS, props<{
  payload: { [matchType: string]: PlayerBattingDetails[] }
}>())

const LOAD_PLAYER_BOWLING_DETAILS = 'LOAD_PLAYER_BOWLING_DETAILS;';
const LOAD_PLAYER_BOWLING_DETAILS_SUCCESS = 'LOAD_PLAYER_BOWLING_DETAILS_SUCCESS;';

export const LoadPlayerBowlingDetailsAction = createAction(LOAD_PLAYER_BOWLING_DETAILS, props<{ payload: number }>())
export const LoadPlayerBowlingDetailsSuccessAction = createAction(LOAD_PLAYER_BOWLING_DETAILS_SUCCESS, props<{
  payload: { [matchType: string]: PlayerBowlingDetails[] }
}>())
