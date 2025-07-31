import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Envelope} from 'src/app/models/envelope';
import {
  LoadByGroundFieldingRecordsAction,
  LoadByGroundFieldingRecordsSuccessAction,
  LoadByHostFieldingRecordsAction,
  LoadByHostFieldingRecordsSuccessAction,
  LoadByMatchFieldingRecordsAction,
  LoadByMatchFieldingRecordsSuccessAction,
  LoadByOppositionFieldingRecordsAction,
  LoadByOppositionFieldingRecordsSuccessAction,
  LoadBySeasonFieldingRecordsAction,
  LoadBySeasonFieldingRecordsSuccessAction,
  LoadBySeriesFieldingRecordsAction,
  LoadBySeriesFieldingRecordsSuccessAction,
  LoadByYearFieldingRecordsAction,
  LoadByYearFieldingRecordsSuccessAction,
  LoadInnByInnFieldingRecordsAction,
  LoadInnByInnFieldingRecordsSuccessAction,
  LoadOverallFieldingRecordsAction,
  LoadOverallFieldingRecordsSuccessAction
} from '../actions/records.actions';
import {catchError, map, mergeMap} from 'rxjs/operators';
import {SqlResultsEnvelope} from '../../../models/sqlresultsenvelope.model';
import {RaiseErrorAction} from '../../../actions/error.actions';
import {createError, handleError} from '../../../helpers/ErrorHelper';
import {of} from 'rxjs';
import {FieldingCareerRecordDto} from '../models/fielding-overall.model';
import {IndividualFieldingDetailsDto} from '../models/individual-fielding-details.dto';
import {FieldingRecordService} from '../services/fielding-record.service';


@Injectable()
export class RecordEffects {
  // todo: in the map check that the envelope is valid and if not return an error
  loadFieldingRecordsOverall$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadOverallFieldingRecordsAction),
      mergeMap(action => this.fieldingRecordsSearchService.getFieldingOverall(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the overall records')})

            return LoadOverallFieldingRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Fielding Overall Records')})))
        ))
    );
  });
  loadFieldingRecordsInningsByInnings$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadInnByInnFieldingRecordsAction),
      mergeMap(action => this.fieldingRecordsSearchService.getFieldingInningsByInnings(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<IndividualFieldingDetailsDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the inningsrecords')})

            return LoadInnByInnFieldingRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Fielding By Innings Records')})))
        ))
    );
  });
  loadFieldingRecordsByMatch$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByMatchFieldingRecordsAction),
      mergeMap(action => this.fieldingRecordsSearchService.getFieldingByMatch(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<IndividualFieldingDetailsDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the match records')})

            return LoadByMatchFieldingRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Fielding By Match Records')})))
        ))
    );
  });
  loadFieldingRecordsBySeries$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadBySeriesFieldingRecordsAction),
      mergeMap(action => this.fieldingRecordsSearchService.getFieldingBySeries(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the seriesrecords')})
            return LoadBySeriesFieldingRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Fielding By Series Records')})))
        ))
    );
  });
  loadFieldingRecordsByGround$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByGroundFieldingRecordsAction),
      mergeMap(action => this.fieldingRecordsSearchService.getFieldingByGround(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the groundrecords')})
            return LoadByGroundFieldingRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Fielding By Ground Records')})))
        ))
    );
  });
  loadFieldingRecordsByHost$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByHostFieldingRecordsAction),
      mergeMap(action => this.fieldingRecordsSearchService.getFieldingByHostCountry(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the host countryrecords')})
            return LoadByHostFieldingRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Fielding By Host Records')})))
        ))
    );
  });
  loadFieldingRecordsByOpposition$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByOppositionFieldingRecordsAction),
      mergeMap(action => this.fieldingRecordsSearchService.getFieldingByOpposition(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the oppositionrecords')})
            return LoadByOppositionFieldingRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Fielding By Opposition Records')})))
        ))
    );
  });
  loadFieldingRecordsByYear$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByYearFieldingRecordsAction),
      mergeMap(action => this.fieldingRecordsSearchService.getFieldingByYear(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the yearlyrecords')})
            return LoadByYearFieldingRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Fielding By Year Records')})))
        ))
    );
  });
  loadFieldingRecordsBySeason$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadBySeasonFieldingRecordsAction),
      mergeMap(action => this.fieldingRecordsSearchService.getFieldingBySeason(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the seasonrecords')})
            return LoadBySeasonFieldingRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Fielding By Season Records')})))
        ))
    );
  });

  constructor(
    private fieldingRecordsSearchService: FieldingRecordService
    , private actions$: Actions
  ) {
  }

}
