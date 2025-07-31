import {createAction, props} from '@ngrx/store';
import {BowlingOverallUiModel, InningsByInningsUiModel} from '../models/bowling-overall-ui.model';
import {FindRecords} from '../../../models/find-records.model';

const LOAD_OVERALL_BOWLINGRECORDS = 'LOAD_OVERALL_BOWLINGRECORDS;';
const LOAD_OVERALL_BOWLINGRECORDS_SUCCESS = 'LOAD_OVERALL_BOWLINGRECORDS_SUCCESS;';

export const LoadOverallBowlingRecordsAction = createAction(LOAD_OVERALL_BOWLINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadOverallBowlingRecordsSuccessAction = createAction(LOAD_OVERALL_BOWLINGRECORDS_SUCCESS, props<{
  payload: BowlingOverallUiModel
}>())

const LOAD_INNBYINN_BOWLINGRECORDS = 'LOAD_INNBYINN_BOWLINGRECORDS;';
const LOAD_INNBYINN_BOWLINGRECORDS_SUCCESS = 'LOAD_INNBYINN_BOWLINGRECORDS_SUCCESS;';

export const LoadInnByInnBowlingRecordsAction = createAction(LOAD_INNBYINN_BOWLINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadInnByInnBowlingRecordsSuccessAction = createAction(LOAD_INNBYINN_BOWLINGRECORDS_SUCCESS, props<{
  payload: InningsByInningsUiModel
}>())

const LOAD_BYMATCH_BOWLINGRECORDS = 'LOAD_BYMATCH_BOWLINGRECORDS;';
const LOAD_BYMATCH_BOWLINGRECORDS_SUCCESS = 'LOAD_BYMATCH_BOWLINGRECORDS_SUCCESS;';

export const LoadByMatchBowlingRecordsAction = createAction(LOAD_BYMATCH_BOWLINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByMatchBowlingRecordsSuccessAction = createAction(LOAD_BYMATCH_BOWLINGRECORDS_SUCCESS, props<{
  payload: InningsByInningsUiModel
}>())

const LOAD_BYSERIES_BOWLINGRECORDS = 'LOAD_BYSERIES_BOWLINGRECORDS;';
const LOAD_BYSERIES_BOWLINGRECORDS_SUCCESS = 'LOAD_BYSERIES_BOWLINGRECORDS_SUCCESS;';

export const LoadBySeriesBowlingRecordsAction = createAction(LOAD_BYSERIES_BOWLINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadBySeriesBowlingRecordsSuccessAction = createAction(LOAD_BYSERIES_BOWLINGRECORDS_SUCCESS, props<{
  payload: BowlingOverallUiModel
}>())

const LOAD_BYGROUND_BOWLINGRECORDS = 'LOAD_BYGROUND_BOWLINGRECORDS;';
const LOAD_BYGROUND_BOWLINGRECORDS_SUCCESS = 'LOAD_BYGROUND_BOWLINGRECORDS_SUCCESS;';

export const LoadByGroundBowlingRecordsAction = createAction(LOAD_BYGROUND_BOWLINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByGroundBowlingRecordsSuccessAction = createAction(LOAD_BYGROUND_BOWLINGRECORDS_SUCCESS, props<{
  payload: BowlingOverallUiModel
}>())

const LOAD_BYHOST_BOWLINGRECORDS = 'LOAD_BYHOST_BOWLINGRECORDS;';
const LOAD_BYHOST_BOWLINGRECORDS_SUCCESS = 'LOAD_BYHOST_BOWLINGRECORDS_SUCCESS;';

export const LoadByHostBowlingRecordsAction = createAction(LOAD_BYHOST_BOWLINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByHostBowlingRecordsSuccessAction = createAction(LOAD_BYHOST_BOWLINGRECORDS_SUCCESS, props<{
  payload: BowlingOverallUiModel
}>())

const LOAD_BYOPPOSITION_BOWLINGRECORDS = 'LOAD_BYOPPOSITION_BOWLINGRECORDS;';
const LOAD_BYOPPOSITION_BOWLINGRECORDS_SUCCESS = 'LOAD_BYOPPOSITION_BOWLINGRECORDS_SUCCESS;';

export const LoadByOppositionBowlingRecordsAction = createAction(LOAD_BYOPPOSITION_BOWLINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByOppositionBowlingRecordsSuccessAction = createAction(LOAD_BYOPPOSITION_BOWLINGRECORDS_SUCCESS, props<{
  payload: BowlingOverallUiModel
}>())

const LOAD_BYSEASON_BOWLINGRECORDS = 'LOAD_BYSEASON_BOWLINGRECORDS;';
const LOAD_BYSEASON_BOWLINGRECORDS_SUCCESS = 'LOAD_BYSEASON_BOWLINGRECORDS_SUCCESS;';

export const LoadBySeasonBowlingRecordsAction = createAction(LOAD_BYSEASON_BOWLINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadBySeasonBowlingRecordsSuccessAction = createAction(LOAD_BYSEASON_BOWLINGRECORDS_SUCCESS, props<{
  payload: BowlingOverallUiModel
}>())

const LOAD_BYYEAR_BOWLINGRECORDS = 'LOAD_BYYEAR_BOWLINGRECORDS;';
const LOAD_BYYEAR_BOWLINGRECORDS_SUCCESS = 'LOAD_BYYEAR_BOWLINGRECORDS_SUCCESS;';

export const LoadByYearBowlingRecordsAction = createAction(LOAD_BYYEAR_BOWLINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByYearBowlingRecordsSuccessAction = createAction(LOAD_BYYEAR_BOWLINGRECORDS_SUCCESS, props<{
  payload: BowlingOverallUiModel
}>())
