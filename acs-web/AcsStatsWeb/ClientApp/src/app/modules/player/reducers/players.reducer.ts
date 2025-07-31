import {Player} from '../models/player';
import {createReducer, on} from '@ngrx/store';
import {
  LoadPlayerBattingDetailsSuccessAction,
  LoadPlayerBiographySuccessAction,
  LoadPlayerBowlingDetailsSuccessAction,
  LoadPlayerOverallSuccessAction,
  LoadPlayersSuccessAction
} from '../actions/players.actions';
import {
  PlayerDetail,
  PlayerBattingDetails,
  PlayerBiography,
  PlayerBowlingDetails,
  PlayerOverall
} from '../playerbiography.model';


export const initialPlayersState = {
  sqlResults: {data: Array<Player>(), count: 0},
  sortOrder: 4,
  sortDirection: 'DESC',
  error: {}
};

export const playersSuccessReducer = createReducer(
  initialPlayersState,
  on(LoadPlayersSuccessAction, (state, players) => {
    return {
      sqlResults: players.payload.sqlResults,
      sortOrder: players.payload.sortOrder,
      sortDirection: players.payload.sortDirection,
      error: {}
    }
  })
);

export const initialPlayerBiographyState = new PlayerBiography(new PlayerDetail("", 0, "", "", "", ""));

export const playerBiographySuccessReducer = createReducer(
  initialPlayerBiographyState,
  on(LoadPlayerBiographySuccessAction, (state, players) => {
    return players.payload
  })
);

export const initialPlayerOverallState: ReadonlyArray<PlayerOverall[]> = [];

export const playerOverallSuccessReducer = createReducer(
  initialPlayerOverallState,
  on(LoadPlayerOverallSuccessAction, (state, players) => {
    return players.payload
  })
);

export const initialPlayerBattingOverallState: { [matchType: string]: PlayerBattingDetails[] } = {};

export const playerBattingOverallSuccessReducer = createReducer(
  initialPlayerBattingOverallState,
  on(LoadPlayerBattingDetailsSuccessAction, (state, players) => {
    return players.payload
  })
);

export const initialPlayerBowlingOverallState: { [matchType: string]: PlayerBowlingDetails[] } = {};

export const playerBowlingOverallSuccessReducer = createReducer(
  initialPlayerBowlingOverallState,
  on(LoadPlayerBowlingDetailsSuccessAction, (state, players) => {
    return players.payload
  })
);
