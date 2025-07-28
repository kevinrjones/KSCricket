import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, map, mergeMap} from 'rxjs/operators';
import {of} from 'rxjs';
import {
  LoadByGroundBattingRecordsAction,
  LoadByGroundBattingRecordsSuccessAction,
  LoadByHostBattingRecordsAction,
  LoadByHostBattingRecordsSuccessAction,
  LoadByMatchBattingRecordsAction,
  LoadByMatchBattingRecordsSuccessAction,
  LoadByOppositionBattingRecordsAction,
  LoadByOppositionBattingRecordsSuccessAction,
  LoadBySeasonBattingRecordsAction,
  LoadBySeasonBattingRecordsSuccessAction,
  LoadBySeriesBattingRecordsAction,
  LoadBySeriesBattingRecordsSuccessAction,
  LoadByYearBattingRecordsAction,
  LoadByYearBattingRecordsSuccessAction,
  LoadInnByInnBattingRecordsAction,
  LoadInnByInnBattingRecordsSuccessAction,
  LoadOverallBattingRecordsAction,
  LoadOverallBattingRecordsSuccessAction
} from '../actions/records.actions';
import {BattingRecordService} from '../services/batting-record.service';
import {createError, handleError} from '../../../helpers/ErrorHelper';
import {RaiseErrorAction} from '../../../actions/error.actions';
import {Envelope} from 'src/app/models/envelope';
import {SqlResultsEnvelope} from '../../../models/sqlresultsenvelope.model';
import {BattingCareerRecordDto} from '../models/batting-overall.model';
import {IndividualBattingDetailsDto} from '../models/individual-batting-details.dto';

@Injectable()
export class RecordEffects {


  loadBattingRecordsOverall$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadOverallBattingRecordsAction),
      mergeMap(action => this.battingRecordsSearchService.getBattingOverall(action.payload)
        .pipe(
          map((battingRecordsResponse: Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>) => {
            if (battingRecordsResponse.errorMessage != null && battingRecordsResponse.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the overall records')})

            return LoadOverallBattingRecordsSuccessAction({
              payload: {
                sqlResults: battingRecordsResponse.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => {
            return of(RaiseErrorAction({payload: handleError(err, 'Getting Batting Overall Records')}))
            }
          )
        ))
    );
  });
  loadBattingRecordsInningsByInnings$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadInnByInnBattingRecordsAction),
      mergeMap(action => this.battingRecordsSearchService.getBattingInningsByInnings(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<IndividualBattingDetailsDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the inningsrecords')})

            return LoadInnByInnBattingRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Batting By Innings Records')})))
        ))
    );
  });
  loadBattingRecordsByMatch$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByMatchBattingRecordsAction),
      mergeMap(action => this.battingRecordsSearchService.getBattingByMatch(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<IndividualBattingDetailsDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the match records')})

            return LoadByMatchBattingRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Batting By Match Records')})))
        ))
    );
  });
  loadBattingRecordsBySeries$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadBySeriesBattingRecordsAction),
      mergeMap(action => this.battingRecordsSearchService.getBattingBySeries(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the seriesrecords')})
            return LoadBySeriesBattingRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Batting By Series Records')})))
        ))
    );
  });
  loadBattingRecordsByGround$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByGroundBattingRecordsAction),
      mergeMap(action => this.battingRecordsSearchService.getBattingByGround(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the groundrecords')})
            return LoadByGroundBattingRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Batting By Ground Records')})))
        ))
    );
  });
  loadBattingRecordsByHost$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByHostBattingRecordsAction),
      mergeMap(action => this.battingRecordsSearchService.getBattingByHostCountry(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the host countryrecords')})
            return LoadByHostBattingRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Batting By Host Records')})))
        ))
    );
  });
  loadBattingRecordsByOpposition$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByOppositionBattingRecordsAction),
      mergeMap(action => this.battingRecordsSearchService.getBattingByOpposition(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the oppositionrecords')})
            return LoadByOppositionBattingRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Batting By Opposition Records')})))
        ))
    );
  });
  loadBattingRecordsByYear$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByYearBattingRecordsAction),
      mergeMap(action => this.battingRecordsSearchService.getBattingByYear(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the yearlyrecords')})
            return LoadByYearBattingRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Batting By Year Records')})))
        ))
    );
  });
  loadBattingRecordsBySeason$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadBySeasonBattingRecordsAction),
      mergeMap(action => this.battingRecordsSearchService.getBattingBySeason(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the seasonrecords')})
            return LoadBySeasonBattingRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Batting By Season Records')})))
        ))
    );
  });

  constructor(
    private readonly battingRecordsSearchService: BattingRecordService,
    private readonly actions$: Actions
  ) {
  }

}
