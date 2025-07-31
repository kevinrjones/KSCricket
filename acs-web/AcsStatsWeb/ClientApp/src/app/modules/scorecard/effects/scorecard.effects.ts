import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, map, mergeMap} from 'rxjs/operators';
import {of} from 'rxjs';
import {
  LoadByDecadeAction,
  LoadByDecadeSuccessAction,
  LoadByYearAction,
  LoadByYearSuccessAction,
  LoadScorecardAction,
  LoadScorecardByIdAction,
  LoadScorecardListAction,
  LoadScorecardListSuccessAction,
  LoadScorecardSuccessAction,
  LoadScorecardTournamentListAction
} from '../actions/scorecard.actions';
import {ScorecardSearchService} from '../services/scorecard-search.service';
import {createError} from '../../../helpers/ErrorHelper';
import {RaiseErrorAction} from '../../../actions/error.actions';

@Injectable()
export class ScorecardEffects {
  // todo: in the map check that the envelope is valid and if not return an error
  loadScorecardList$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadScorecardListAction),
      mergeMap(action => this.scorecardSearchService.findMatches(action.payload)
        .pipe(
          map(players => LoadScorecardListSuccessAction({payload: players.result})),
          catchError((err) => of(RaiseErrorAction({payload: createError(1, 'Unable to get list of scorecards')})))
        ))
    );
  });
  loadTournaments$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadScorecardTournamentListAction),
      mergeMap(action => this.scorecardSearchService.findTournament(action.payload)
        .pipe(
          map(players => LoadScorecardListSuccessAction({payload: players.result})),
          catchError((err) => of(RaiseErrorAction({payload: createError(1, 'Unable to get list of tournaments')})))
        ))
    );
  });
  loadScorecard$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadScorecardAction),
      mergeMap(action => this.scorecardSearchService.findCard(action.payload)
        .pipe(
          map(scorecard => LoadScorecardSuccessAction({payload: scorecard.result})),
          catchError((err) => of(RaiseErrorAction({payload: createError(1, 'Unable to get scorecard')})))
        ))
    );
  });
  loadScorecardById$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadScorecardByIdAction),
      mergeMap(action => this.scorecardSearchService.findCardById(action.payload)
        .pipe(
          map(scorecard => LoadScorecardSuccessAction({payload: scorecard.result})),
          catchError((err) => of(RaiseErrorAction({payload: createError(1, 'Unable to get scorecard')})))
        ))
    );
  });
  loadByDecade$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByDecadeAction),
      mergeMap(action => this.scorecardSearchService.findByDecade(action.payload)
        .pipe(
          map(decades => LoadByDecadeSuccessAction({payload: decades.result})),
          catchError((err) => of(RaiseErrorAction({payload: createError(1, 'Unable to get dates')})))
        ))
    );
  });
  loadByYear$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByYearAction),
      mergeMap(action => this.scorecardSearchService.findByYear(action.payload.year, action.payload.type)
        .pipe(
          map(decades => LoadByYearSuccessAction({payload: decades.result})),
          catchError((err) => of(RaiseErrorAction({payload: createError(1, 'Unable to get dates')})))
        ))
    );
  });

  constructor(
    private scorecardSearchService: ScorecardSearchService
    , private actions$: Actions
  ) {
  }

}
