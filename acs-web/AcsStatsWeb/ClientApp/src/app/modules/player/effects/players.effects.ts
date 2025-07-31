import {Injectable} from '@angular/core';
import {PlayerSearchService} from '../services/playersearch.service';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, map, mergeMap} from 'rxjs/operators';
import {of} from 'rxjs';
import {
  LoadPlayerBattingDetailsAction,
  LoadPlayerBattingDetailsSuccessAction,
  LoadPlayerBiographyAction,
  LoadPlayerBiographySuccessAction,
  LoadPlayerBowlingDetailsAction,
  LoadPlayerBowlingDetailsSuccessAction,
  LoadPlayerOverallAction,
  LoadPlayerOverallSuccessAction,
  LoadPlayersAction,
  LoadPlayersSuccessAction
} from '../actions/players.actions';
import {createError} from '../../../helpers/ErrorHelper';
import {RaiseErrorAction} from '../../../actions/error.actions';
import {Envelope} from '../../../models/envelope';
import {SqlResultsEnvelope} from '../../../models/sqlresultsenvelope.model';
import {Player} from '../models/player';

@Injectable()
export class PlayerEffects {
  // todo: in the map check that the envelope is valid and if not return an error
  loadPlayers$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadPlayersAction),
      mergeMap(action => this.playerSearchService.findPlayers(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<Player[]>>) => LoadPlayersSuccessAction({
            payload: {
              sqlResults: players.result,
              sortOrder: action.payload.sortOrder,
              sortDirection: action.payload.sortDirection
            }
          })),
          catchError((err) => of(RaiseErrorAction({payload: createError(1, 'Unable to get players')})))
        ))
    );
  });
  loadPlayerBiography$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadPlayerBiographyAction),
      mergeMap(action => this.playerSearchService.getPlayerBiography(action.payload)
        .pipe(
          map(players => LoadPlayerBiographySuccessAction({payload: players.result})),
          catchError((err) => of(RaiseErrorAction({payload: createError(1, 'Unable to get player biography')})))
        ))
    );
  });
  loadPlayerOverall$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadPlayerOverallAction),
      mergeMap(action => this.playerSearchService.getPlayerOverall(action.payload)
        .pipe(
          map(players => LoadPlayerOverallSuccessAction({payload: players.result})),
          catchError((err) => of(RaiseErrorAction({payload: createError(1, 'Unable to get player')})))
        ))
    );
  });
  loadPlayerBattingOverall$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadPlayerBattingDetailsAction),
      mergeMap(action => this.playerSearchService.getPlayerBattingOverall(action.payload)
        .pipe(
          map(players => LoadPlayerBattingDetailsSuccessAction({payload: players.result})),
          catchError((err) => of(RaiseErrorAction({payload: createError(1, 'Unable to get player batting')})))
        ))
    );
  });
  loadPlayerBowlingOverall$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadPlayerBowlingDetailsAction),
      mergeMap(action => this.playerSearchService.getPlayerBowlingOverall(action.payload)
        .pipe(
          map(players => LoadPlayerBowlingDetailsSuccessAction({payload: players.result})),
          catchError((err) => of(RaiseErrorAction({payload: createError(1, 'Unable to get player bowling')})))
        ))
    );
  });

  constructor(
    private playerSearchService: PlayerSearchService
    , private actions$: Actions
  ) {
  }

}
