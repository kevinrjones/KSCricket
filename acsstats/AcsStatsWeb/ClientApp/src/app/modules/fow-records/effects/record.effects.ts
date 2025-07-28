import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, map, mergeMap} from 'rxjs/operators';
import {of} from 'rxjs';
import {
  LoadByGroundFowRecordsAction,
  LoadByGroundFowRecordsSuccessAction,
  LoadByHostFowRecordsAction,
  LoadByHostFowRecordsSuccessAction,
  LoadByMatchFowRecordsAction,
  LoadByMatchFowRecordsSuccessAction,
  LoadByOppositionFowRecordsAction,
  LoadByOppositionFowRecordsSuccessAction,
  LoadBySeasonFowRecordsAction,
  LoadBySeasonFowRecordsSuccessAction,
  LoadBySeriesFowRecordsAction,
  LoadBySeriesFowRecordsSuccessAction,
  LoadByYearFowRecordsAction,
  LoadByYearFowRecordsSuccessAction,
  LoadInnByInnForWicketFowRecordsAction,
  LoadInnByInnFowRecordsAction,
  LoadInnByInnFowRecordsSuccessAction,
  LoadOverallFowRecordsAction,
  LoadOverallFowRecordsSuccessAction
} from '../actions/records.actions';
import {FowRecordService} from '../services/fow-record.service';
import {createError, handleError} from '../../../helpers/ErrorHelper';
import {RaiseErrorAction} from '../../../actions/error.actions';
import {Envelope} from 'src/app/models/envelope';
import {SqlResultsEnvelope} from '../../../models/sqlresultsenvelope.model';
import {FowCareerRecordDto} from '../models/fow-overall.model';
import {IndividualFowDetailsDto} from '../models/individual-fow-details.dto';

@Injectable()
export class RecordEffects {
  // todo: in the map check that the envelope is valid and if not return an error
  loadFowRecordsOverall$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadOverallFowRecordsAction),
      mergeMap(action => this.fowRecordsSearchService.getFowOverall(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the overall records')})

            return LoadOverallFowRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Fow Overall Records')})))
        ))
    );
  });
  loadFowRecordsInningsByInnings$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadInnByInnFowRecordsAction),
      mergeMap(action => this.fowRecordsSearchService.getFowInningsByInnings(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<IndividualFowDetailsDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the inningsrecords')})

            return LoadInnByInnFowRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Fow By Innings Records')})))
        ))
    );
  });
  loadFowRecordsInningsByInningsForWicket$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadInnByInnForWicketFowRecordsAction),
      mergeMap(action => this.fowRecordsSearchService.getFowInningsByInningsByWicket(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<IndividualFowDetailsDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the inningsrecords')})

            return LoadInnByInnFowRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Fow By Innings Records')})))
        ))
    );
  });
  loadFowRecordsByMatch$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByMatchFowRecordsAction),
      mergeMap(action => this.fowRecordsSearchService.getFowByMatch(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<IndividualFowDetailsDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the match records')})

            return LoadByMatchFowRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Fow By Match Records')})))
        ))
    );
  });
  loadFowRecordsBySeries$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadBySeriesFowRecordsAction),
      mergeMap(action => this.fowRecordsSearchService.getFowBySeries(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the seriesrecords')})
            return LoadBySeriesFowRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Fow By Series Records')})))
        ))
    );
  });
  loadFowRecordsByGround$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByGroundFowRecordsAction),
      mergeMap(action => this.fowRecordsSearchService.getFowByGround(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the groundrecords')})
            return LoadByGroundFowRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Fow By Ground Records')})))
        ))
    );
  });
  loadFowRecordsByHost$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByHostFowRecordsAction),
      mergeMap(action => this.fowRecordsSearchService.getFowByHostCountry(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the host countryrecords')})
            return LoadByHostFowRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Fow By Host Records')})))
        ))
    );
  });
  loadFowRecordsByOpposition$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByOppositionFowRecordsAction),
      mergeMap(action => this.fowRecordsSearchService.getFowByOpposition(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the oppositionrecords')})
            return LoadByOppositionFowRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Fow By Opposition Records')})))
        ))
    );
  });
  loadFowRecordsByYear$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadByYearFowRecordsAction),
      mergeMap(action => this.fowRecordsSearchService.getFowByYear(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the yearlyrecords')})
            return LoadByYearFowRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Fow By Year Records')})))
        ))
    );
  });
  loadFowRecordsBySeason$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadBySeasonFowRecordsAction),
      mergeMap(action => this.fowRecordsSearchService.getFowBySeason(action.payload)
        .pipe(
          map((players: Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>) => {
            if (players.errorMessage != null && players.errorMessage != '')
              return RaiseErrorAction({payload: createError(2, 'Unable to get the seasonrecords')})
            return LoadBySeasonFowRecordsSuccessAction({
              payload: {
                sqlResults: players.result,
                sortOrder: action.payload.sortOrder,
                sortDirection: action.payload.sortDirection
              }
            })
          }),
          catchError((err) => of(RaiseErrorAction({payload: handleError(err, 'Getting Fow By Season Records')})))
        ))
    );
  });

  constructor(
    private fowRecordsSearchService: FowRecordService
    , private actions$: Actions
  ) {
  }

}
