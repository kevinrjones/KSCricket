import {createReducer, on} from '@ngrx/store';
import {
  LoadByDecadeSuccessAction,
  LoadByYearSuccessAction,
  LoadScorecardListSuccessAction,
  LoadScorecardSuccessAction,
} from '../actions/scorecard.actions';
import {ScorecardListItem} from '../models/scorecard-list-item.model';
import {Scorecard} from '../models/scorecard.model';
import {VictoryType} from "../../shared/models/victoryType";



export const scorecardsListInitialState: ReadonlyArray<ScorecardListItem> = [];
export const scorecardListSuccessReducer = createReducer(
  scorecardsListInitialState,
  on(LoadScorecardListSuccessAction, (state, scorecard) => {
    return scorecard.payload
  })
);

export const scorecardTournamentListSuccessReducer = createReducer(
  scorecardsListInitialState,
  on(LoadScorecardListSuccessAction, (state, scorecard) => {
    return scorecard.payload
  })
);

export const scorecardInitialState: Scorecard = new Scorecard([], {
  awayTeam: {key: 0, name: 'unknown'},
  awayTeamScores: [],
  ballsPerOver: 0,
  closeOfPlay: [],
  dayNight: false,
  homeTeam: {key: 0, name: 'unknown'},
  homeTeamScores: [],
  matchDate: '',
  matchDesignator: '',
  matchReferee: [],
  matchTitle: '',
  matchType: '',
  result: {
    whoWon: {key: 0, name: 'unknown'},
    whoLost: {key: 0, name: 'unknown'},
    resultString: {key: 0, name: 'unknown'},
    victoryType: VictoryType.Unknown,
  },
  scorers: [],
  seriesDate: '',
  toss: {key: 0, name: 'unknown'},
  tvUmpires: [],
  playerOfMatch: [],
  umpires: [],
  where: {key: 0, name: 'unknown'}
}, []);
export const scorecardSuccessReducer = createReducer(
  scorecardInitialState,
  on(LoadScorecardSuccessAction, (state, scorecard) => {
    return scorecard.payload
  })
);


export const initialByDecadeState: { [matchType: string]: string[] } = {};

export const scorecardByDecadesReducer = createReducer(
  initialByDecadeState,
  on(LoadByDecadeSuccessAction, (state, scorecard) => {
    return scorecard.payload
  })
);


export const initialByYearState: string[] = []

export const scorecardByYearReducer = createReducer(
  initialByYearState,
  on(LoadByYearSuccessAction, (state, years) => {
    return years.payload
  })
);


