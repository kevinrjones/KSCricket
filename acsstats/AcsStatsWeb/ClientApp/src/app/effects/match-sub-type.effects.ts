import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, map, mergeMap} from 'rxjs/operators';
import {of} from 'rxjs';
import {MatchSubTypeSearchService} from '../services/match-sub-type-search.service';
import {LoadMatchSubTypesAction, LoadMatchSubTypesSuccessAction} from '../actions/match-sub-types.actions';
import {createError} from '../helpers/ErrorHelper';
import {RaiseErrorAction} from '../actions/error.actions';

@Injectable()
export class MatchSubTypeEffects {
  // todo: in the map check that the envelope is valid and if not return an error
  loadMatchSubType$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadMatchSubTypesAction),
      mergeMap(action => this.subMatchSearchService.getMatchSubTypesForMatchType(action.payload)
        .pipe(
          map((matchSubType) => LoadMatchSubTypesSuccessAction({payload: matchSubType})),
          catchError((err) => of(RaiseErrorAction({payload: createError(3, 'Unable to get tournament types')})))
        ))
    );
  });

  constructor(
    private subMatchSearchService: MatchSubTypeSearchService
    , private actions$: Actions
  ) {
  }

}
