import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, map, mergeMap} from 'rxjs/operators';
import {of} from 'rxjs';
import {RecordHelperService} from '../services/record-helper.service';
import {LoadRecordSummariesAction, LoadRecordSummariesSuccessAction} from '../actions/recordsummary.actions';
import {createError, handleError} from '../helpers/ErrorHelper';
import {RaiseErrorAction} from '../actions/error.actions';
import {Envelope} from '../models/envelope';
import {RecordsSummaryModel} from '../models/records-summary.model';
import {DateTime} from 'luxon';
import {MatchSubTypeSearchService} from '../services/match-sub-type-search.service';

@Injectable()
export class RecordSummaryEffects {
  // todo: in the map check that the envelope is valid and if not return an error
  loadrecordSummary$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadRecordSummariesAction),
      mergeMap(action =>
        this.recordHelperService.getSummary(
          action.payload.matchType,
          action.payload.teamId,
          action.payload.opponentsId,
          action.payload.groundId,
          action.payload.hostCountryId)
          .pipe(
            map((summaryModelEnvelope: Envelope<RecordsSummaryModel>) => {
              if (summaryModelEnvelope.errorMessage != null && summaryModelEnvelope.errorMessage != '')
                return RaiseErrorAction({payload: createError(2, 'Unable to get the record summary')})

              let result = this.getResultStringFromResultsValues(action.payload.result);

              let startDate = DateTime.fromSeconds(parseInt(action.payload.startDate))
              let endDate = DateTime.fromSeconds(parseInt(action.payload.endDate))

              summaryModelEnvelope.result.startDate = startDate.toLocaleString(DateTime.DATE_FULL)
              summaryModelEnvelope.result.endDate = endDate.toLocaleString(DateTime.DATE_FULL)

              summaryModelEnvelope.result.season = action.payload.season == '0' ? 'All Seasons' : action.payload.season
              summaryModelEnvelope.result.result = result
              summaryModelEnvelope.result.limit = action.payload.limit

              summaryModelEnvelope.result.matchSubType = this.matchSubTypeService.getMatchSubType(action.payload.matchSubType)

              return LoadRecordSummariesSuccessAction({payload: summaryModelEnvelope.result})
            }),
            catchError((err: any) => {
              let error = handleError(err, 'Unknown Error')
              return of(RaiseErrorAction({payload: error}))
            })
          ))
    );
  });

  constructor(
    private recordHelperService: RecordHelperService,
    private matchSubTypeService: MatchSubTypeSearchService
    , private actions$: Actions
  ) {
  }

  private getResultStringFromResultsValues(result: number): string {
    let resultStr = result == 0 ? 'all results' : ''

    if (result != 0) {
      if (result == 1) resultStr = 'won'
      if (result == 2) resultStr = 'lost'
      if (result == 3) resultStr = 'won & lost'
      if (result == 4) resultStr = 'no result'
      if (result == 5) resultStr = 'won & no result'
      if (result == 6) resultStr = 'lost & no result'
      if (result == 7) resultStr = 'won, lost & no result'
      if (result == 8) resultStr = 'tied'
      if (result == 9) resultStr = 'won & tied'
      if (result == 10) resultStr = 'lost & tied'
      if (result == 11) resultStr = 'won, lost & tied'
      if (result == 12) resultStr = 'no result & tied'
      if (result == 13) resultStr = 'won, no result & tied'
      if (result == 14) resultStr = 'lost, no result  & tied'
      if (result == 15) resultStr = 'all results'
    }

    return resultStr
  }
}
