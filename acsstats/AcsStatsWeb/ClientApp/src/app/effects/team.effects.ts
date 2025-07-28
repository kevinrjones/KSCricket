import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {TeamSearchService} from '../services/teamsearch.service';
import {catchError, map, mergeMap} from 'rxjs/operators';
import {of} from 'rxjs';
import {LoadTeamsAction, LoadTeamsSuccessAction} from '../actions/teams.actions';
import {createError} from '../helpers/ErrorHelper';
import {RaiseErrorAction} from '../actions/error.actions';
import {Envelope} from '../models/envelope';
import {Team} from '../models/team.model';

@Injectable()
export class TeamEffects {
  loadTeamsEx$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadTeamsAction),
      mergeMap(action => this.teamSearchService.findTeamsForMatchTypeAndCountry(action.payload.matchType, action.payload.countryId)
        .pipe(
          map((teams: Envelope<Team[]>) => {
            if (teams.errorMessage != null && teams.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get teams')})

            return LoadTeamsSuccessAction({payload: teams.result})
          }),
          catchError((err) => of(RaiseErrorAction({payload: createError(1, 'Unable to get teams')})))
        ))
    );
  });

  constructor(
    private teamSearchService: TeamSearchService
    , private actions$: Actions
  ) {
  }

}
