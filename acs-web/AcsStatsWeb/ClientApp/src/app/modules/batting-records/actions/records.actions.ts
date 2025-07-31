import {createAction, props} from '@ngrx/store';
import {BattingOverallUiModel, InningsByInningsUiModel} from '../models/batting-overall-ui.model';
import {FindRecords} from '../../../models/find-records.model';

const LOAD_OVERALL_BATTINGRECORDS = 'LOAD_OVERALL_BATTINGRECORDS;';
const LOAD_OVERALL_BATTINGRECORDS_SUCCESS = 'LOAD_OVERALL_BATTINGRECORDS_SUCCESS;';

export const LoadOverallBattingRecordsAction = createAction(LOAD_OVERALL_BATTINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadOverallBattingRecordsSuccessAction = createAction(LOAD_OVERALL_BATTINGRECORDS_SUCCESS, props<{
  payload: BattingOverallUiModel
}>())

const LOAD_INNBYINN_BATTINGRECORDS = 'LOAD_INNBYINN_BATTINGRECORDS;';
const LOAD_INNBYINN_BATTINGRECORDS_SUCCESS = 'LOAD_INNBYINN_BATTINGRECORDS_SUCCESS;';

export const LoadInnByInnBattingRecordsAction = createAction(LOAD_INNBYINN_BATTINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadInnByInnBattingRecordsSuccessAction = createAction(LOAD_INNBYINN_BATTINGRECORDS_SUCCESS, props<{
  payload: InningsByInningsUiModel
}>())

const LOAD_BYMATCH_BATTINGRECORDS = 'LOAD_BYMATCH_BATTINGRECORDS;';
const LOAD_BYMATCH_BATTINGRECORDS_SUCCESS = 'LOAD_BYMATCH_BATTINGRECORDS_SUCCESS;';

export const LoadByMatchBattingRecordsAction = createAction(LOAD_BYMATCH_BATTINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByMatchBattingRecordsSuccessAction = createAction(LOAD_BYMATCH_BATTINGRECORDS_SUCCESS, props<{
  payload: InningsByInningsUiModel
}>())

const LOAD_BYSERIES_BATTINGRECORDS = 'LOAD_BYSERIES_BATTINGRECORDS;';
const LOAD_BYSERIES_BATTINGRECORDS_SUCCESS = 'LOAD_BYSERIES_BATTINGRECORDS_SUCCESS;';

export const LoadBySeriesBattingRecordsAction = createAction(LOAD_BYSERIES_BATTINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadBySeriesBattingRecordsSuccessAction = createAction(LOAD_BYSERIES_BATTINGRECORDS_SUCCESS, props<{
  payload: BattingOverallUiModel
}>())

const LOAD_BYGROUND_BATTINGRECORDS = 'LOAD_BYGROUND_BATTINGRECORDS;';
const LOAD_BYGROUND_BATTINGRECORDS_SUCCESS = 'LOAD_BYGROUND_BATTINGRECORDS_SUCCESS;';

export const LoadByGroundBattingRecordsAction = createAction(LOAD_BYGROUND_BATTINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByGroundBattingRecordsSuccessAction = createAction(LOAD_BYGROUND_BATTINGRECORDS_SUCCESS, props<{
  payload: BattingOverallUiModel
}>())

const LOAD_BYHOST_BATTINGRECORDS = 'LOAD_BYHOST_BATTINGRECORDS;';
const LOAD_BYHOST_BATTINGRECORDS_SUCCESS = 'LOAD_BYHOST_BATTINGRECORDS_SUCCESS;';

export const LoadByHostBattingRecordsAction = createAction(LOAD_BYHOST_BATTINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByHostBattingRecordsSuccessAction = createAction(LOAD_BYHOST_BATTINGRECORDS_SUCCESS, props<{
  payload: BattingOverallUiModel
}>())

const LOAD_BYOPPOSITION_BATTINGRECORDS = 'LOAD_BYOPPOSITION_BATTINGRECORDS;';
const LOAD_BYOPPOSITION_BATTINGRECORDS_SUCCESS = 'LOAD_BYOPPOSITION_BATTINGRECORDS_SUCCESS;';

export const LoadByOppositionBattingRecordsAction = createAction(LOAD_BYOPPOSITION_BATTINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByOppositionBattingRecordsSuccessAction = createAction(LOAD_BYOPPOSITION_BATTINGRECORDS_SUCCESS, props<{
  payload: BattingOverallUiModel
}>())

const LOAD_BYSEASON_BATTINGRECORDS = 'LOAD_BYSEASON_BATTINGRECORDS;';
const LOAD_BYSEASON_BATTINGRECORDS_SUCCESS = 'LOAD_BYSEASON_BATTINGRECORDS_SUCCESS;';

export const LoadBySeasonBattingRecordsAction = createAction(LOAD_BYSEASON_BATTINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadBySeasonBattingRecordsSuccessAction = createAction(LOAD_BYSEASON_BATTINGRECORDS_SUCCESS, props<{
  payload: BattingOverallUiModel
}>())

const LOAD_BYYEAR_BATTINGRECORDS = 'LOAD_BYYEAR_BATTINGRECORDS;';
const LOAD_BYYEAR_BATTINGRECORDS_SUCCESS = 'LOAD_BYYEAR_BATTINGRECORDS_SUCCESS;';

export const LoadByYearBattingRecordsAction = createAction(LOAD_BYYEAR_BATTINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByYearBattingRecordsSuccessAction = createAction(LOAD_BYYEAR_BATTINGRECORDS_SUCCESS, props<{
  payload: BattingOverallUiModel
}>())

