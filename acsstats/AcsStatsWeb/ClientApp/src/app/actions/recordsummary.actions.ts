import {createAction, props} from '@ngrx/store'
import {RecordsSummaryModel} from '../models/records-summary.model';

export const LOAD_RECORDSUMMARIES = 'LOAD_RECORDSUMMARIES;';
export const LOAD_RECORDSUMMARIES_SUCCESS = 'LOAD_RECORDSUMMARIES_SUCCESS;';

export const LoadRecordSummariesAction = createAction(LOAD_RECORDSUMMARIES, props<{
  payload: {
    startDate: string,
    endDate: string,
    season: string,
    result: number,
    limit: number,
    matchType: string,
    matchSubType: string,
    teamId: number,
    opponentsId: number,
    groundId: number,
    hostCountryId: number
  }
}>())
export const LoadRecordSummariesSuccessAction = createAction(LOAD_RECORDSUMMARIES_SUCCESS, props<{
  payload: RecordsSummaryModel
}>())


