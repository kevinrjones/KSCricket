import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {TeamRecordsService} from '../services/team-record.service';
import {Envelope} from 'src/app/models/envelope';
import {
  LoadByGroundTeamRecordsAction,
  LoadByGroundTeamRecordsSuccessAction,
  LoadByHighestTargetChasedTeamRecordsAction,
  LoadByHighestTargetChasedTeamRecordsSuccessAction,
  LoadByHostTeamRecordsAction,
  LoadByHostTeamRecordsSuccessAction,
  LoadByInningsExtrasTeamRecordsAction,
  LoadByInningsExtrasTeamRecordsSuccessAction,
  LoadByLowestTargetDefendedTeamRecordsAction,
  LoadByLowestTargetDefendedTeamRecordsSuccessAction,
  LoadByLowestUnreducedTargetDefendedTeamRecordsAction,
  LoadByLowestUnreducedTargetDefendedTeamRecordsSuccessAction,
  LoadByMatchTeamRecordsAction,
  LoadByMatchTeamRecordsSuccessAction,
  LoadByOppositionTeamRecordsAction,
  LoadByOppositionTeamRecordsSuccessAction,
  LoadByResultsTeamRecordsAction,
  LoadByResultsTeamRecordsSuccessAction,
  LoadBySeasonTeamRecordsAction,
  LoadBySeasonTeamRecordsSuccessAction,
  LoadBySeriesTeamRecordsAction,
  LoadBySeriesTeamRecordsSuccessAction,
  LoadByYearTeamRecordsAction,
  LoadByYearTeamRecordsSuccessAction,
  LoadInnByInnTeamRecordsAction,
  LoadInnByInnTeamRecordsSuccessAction,
  LoadOverallExtrasTeamRecordsAction,
  LoadOverallExtrasTeamRecordsSuccessAction,
  LoadOverallTeamRecordsAction,
  LoadOverallTeamRecordsSuccessAction
} from '../actions/records.actions';
import {catchError, map, mergeMap} from 'rxjs/operators';
import {SqlResultsEnvelope} from '../../../models/sqlresultsenvelope.model';
import {RaiseErrorAction} from '../../../actions/error.actions';
import {createError, handleError} from '../../../helpers/ErrorHelper';
import {of} from 'rxjs';
import {TeamRecordDto} from '../models/team-overall.model';
import {IndividualTeamDetailsDto} from '../models/individual-team-details.dto';
import {MatchResultsDto} from '../models/match-results.dto';
import {ByInningsExtrasDto} from '../models/by.InningsExtras.dto';
import {OverallExtrasDto} from '../models/overallExtras.dto';
import {ByTargetDto} from '../models/by-target.dto';

@Injectable()
export class RecordEffects {
  loadTeamRecordsOverall$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadOverallTeamRecordsAction),
      mergeMap(action => this.teamRecordsSearchService.getTeamOverall(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<TeamRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the overall records')})

            return LoadOverallTeamRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Team Overall Records')})))
        ))
    );
  });
  loadTeamRecordsInningsByInnings$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadInnByInnTeamRecordsAction),
      mergeMap(action => this.teamRecordsSearchService.getTeamInningsByInnings(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<IndividualTeamDetailsDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the inningsrecords')})

            return LoadInnByInnTeamRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Team By Innings Records')})))
        ))
    );
  });
  loadTeamRecordsByMatchResults$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByResultsTeamRecordsAction),
      mergeMap(action => this.teamRecordsSearchService.getMatchResults(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<MatchResultsDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the match records')})

            return LoadByResultsTeamRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Team By Match Records')})))
        ))
    );
  });
  loadTeamRecordsByMatchTotals$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByMatchTeamRecordsAction),
      mergeMap(action => this.teamRecordsSearchService.getMatchTotals(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<IndividualTeamDetailsDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the match records')})

            return LoadByMatchTeamRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Team By Match Records')})))
        ))
    );
  });
  loadTeamRecordsBySeries$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadBySeriesTeamRecordsAction),
      mergeMap(action => this.teamRecordsSearchService.getTeamBySeries(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<TeamRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the seriesrecords')})
            return LoadBySeriesTeamRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Team By Series Records')})))
        ))
    );
  });
  loadTeamRecordsByGround$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByGroundTeamRecordsAction),
      mergeMap(action => this.teamRecordsSearchService.getTeamByGround(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<TeamRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the groundrecords')})
            return LoadByGroundTeamRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Team By Ground Records')})))
        ))
    );
  });
  loadTeamRecordsByHost$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByHostTeamRecordsAction),
      mergeMap(action => this.teamRecordsSearchService.getTeamByHostCountry(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<TeamRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the host countryrecords')})
            return LoadByHostTeamRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Team By Host Records')})))
        ))
    );
  });
  loadTeamRecordsByOpposition$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByOppositionTeamRecordsAction),
      mergeMap(action => this.teamRecordsSearchService.getTeamByOpposition(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<TeamRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the oppositionrecords')})
            return LoadByOppositionTeamRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Team By Opposition Records')})))
        ))
    );
  });
  loadTeamRecordsByYear$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByYearTeamRecordsAction),
      mergeMap(action => this.teamRecordsSearchService.getTeamByYear(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<TeamRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the yearlyrecords')})
            return LoadByYearTeamRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Team By Year Records')})))
        ))
    );
  });
  loadTeamRecordsBySeason$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadBySeasonTeamRecordsAction),
      mergeMap(action => this.teamRecordsSearchService.getTeamBySeason(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<TeamRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the seasonrecords')})
            return LoadBySeasonTeamRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Team By Season Records')})))
        ))
    );
  });
  loadTeamRecordsExtrasOverall$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadOverallExtrasTeamRecordsAction),
      mergeMap(action => this.teamRecordsSearchService.getOverallExtras(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<OverallExtrasDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the overall extras')})
            return LoadOverallExtrasTeamRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Team Overall Extras Records')})))
        ))
    );
  });
  loadTeamRecordsExtrasByInnings$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByInningsExtrasTeamRecordsAction),
      mergeMap(action => this.teamRecordsSearchService.getInningsExtras(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<ByInningsExtrasDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the overall extras')})
            return LoadByInningsExtrasTeamRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Team Overall Extras Records')})))
        ))
    );
  });
  loadTeamRecordsHighestTargetChased$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByHighestTargetChasedTeamRecordsAction),
      mergeMap(action => this.teamRecordsSearchService.getHighestTargetChased(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<ByTargetDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the highest targets chased')})
            return LoadByHighestTargetChasedTeamRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Highest Targets Chased Records')})))
        ))
    );
  });
  loadTeamRecordsLowestTargetChased$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByLowestTargetDefendedTeamRecordsAction),
      mergeMap(action => this.teamRecordsSearchService.getLowestTargetChased(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<ByTargetDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the highest targets chansed')})
            return LoadByLowestTargetDefendedTeamRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Highest Targets Chased Records')})))
        ))
    );
  });
  loadTeamRecordsLowestUnreducedTargetChased$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByLowestUnreducedTargetDefendedTeamRecordsAction),
      mergeMap(action => this.teamRecordsSearchService.getLowestUnreducedTargetChased(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<ByTargetDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the highest targets chansed')})
            return LoadByLowestUnreducedTargetDefendedTeamRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Highest Targets Chased Records')})))
        ))
    );
  });

  constructor(
    private teamRecordsSearchService: TeamRecordsService
    , private actions$: Actions
  ) {
  }

}
