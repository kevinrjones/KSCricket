import {createAction, props} from '@ngrx/store';
import {FindRecords} from '../../../models/find-records.model';
import {FieldingOverallUiModel, InningsByInningsUiModel} from '../models/fielding-overall-ui.model';

const LOAD_OVERALL_FIELDINGRECORDS = 'LOAD_OVERALL_FIELDINGRECORDS;';
const LOAD_OVERALL_FIELDINGRECORDS_SUCCESS = 'LOAD_OVERALL_FIELDINGRECORDS_SUCCESS;';

export const LoadOverallFieldingRecordsAction = createAction(LOAD_OVERALL_FIELDINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadOverallFieldingRecordsSuccessAction = createAction(LOAD_OVERALL_FIELDINGRECORDS_SUCCESS, props<{
  payload: FieldingOverallUiModel
}>())

const LOAD_INNBYINN_FIELDINGRECORDS = 'LOAD_INNBYINN_FIELDINGRECORDS;';
const LOAD_INNBYINN_FIELDINGRECORDS_SUCCESS = 'LOAD_INNBYINN_FIELDINGRECORDS_SUCCESS;';

export const LoadInnByInnFieldingRecordsAction = createAction(LOAD_INNBYINN_FIELDINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadInnByInnFieldingRecordsSuccessAction = createAction(LOAD_INNBYINN_FIELDINGRECORDS_SUCCESS, props<{
  payload: InningsByInningsUiModel
}>())

const LOAD_BYMATCH_FIELDINGRECORDS = 'LOAD_BYMATCH_FIELDINGRECORDS;';
const LOAD_BYMATCH_FIELDINGRECORDS_SUCCESS = 'LOAD_BYMATCH_FIELDINGRECORDS_SUCCESS;';

export const LoadByMatchFieldingRecordsAction = createAction(LOAD_BYMATCH_FIELDINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByMatchFieldingRecordsSuccessAction = createAction(LOAD_BYMATCH_FIELDINGRECORDS_SUCCESS, props<{
  payload: InningsByInningsUiModel
}>())

const LOAD_BYSERIES_FIELDINGRECORDS = 'LOAD_BYSERIES_FIELDINGRECORDS;';
const LOAD_BYSERIES_FIELDINGRECORDS_SUCCESS = 'LOAD_BYSERIES_FIELDINGRECORDS_SUCCESS;';

export const LoadBySeriesFieldingRecordsAction = createAction(LOAD_BYSERIES_FIELDINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadBySeriesFieldingRecordsSuccessAction = createAction(LOAD_BYSERIES_FIELDINGRECORDS_SUCCESS, props<{
  payload: FieldingOverallUiModel
}>())

const LOAD_BYGROUND_FIELDINGRECORDS = 'LOAD_BYGROUND_FIELDINGRECORDS;';
const LOAD_BYGROUND_FIELDINGRECORDS_SUCCESS = 'LOAD_BYGROUND_FIELDINGRECORDS_SUCCESS;';

export const LoadByGroundFieldingRecordsAction = createAction(LOAD_BYGROUND_FIELDINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByGroundFieldingRecordsSuccessAction = createAction(LOAD_BYGROUND_FIELDINGRECORDS_SUCCESS, props<{
  payload: FieldingOverallUiModel
}>())

const LOAD_BYHOST_FIELDINGRECORDS = 'LOAD_BYHOST_FIELDINGRECORDS;';
const LOAD_BYHOST_FIELDINGRECORDS_SUCCESS = 'LOAD_BYHOST_FIELDINGRECORDS_SUCCESS;';

export const LoadByHostFieldingRecordsAction = createAction(LOAD_BYHOST_FIELDINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByHostFieldingRecordsSuccessAction = createAction(LOAD_BYHOST_FIELDINGRECORDS_SUCCESS, props<{
  payload: FieldingOverallUiModel
}>())

const LOAD_BYOPPOSITION_FIELDINGRECORDS = 'LOAD_BYOPPOSITION_FIELDINGRECORDS;';
const LOAD_BYOPPOSITION_FIELDINGRECORDS_SUCCESS = 'LOAD_BYOPPOSITION_FIELDINGRECORDS_SUCCESS;';

export const LoadByOppositionFieldingRecordsAction = createAction(LOAD_BYOPPOSITION_FIELDINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByOppositionFieldingRecordsSuccessAction = createAction(LOAD_BYOPPOSITION_FIELDINGRECORDS_SUCCESS, props<{
  payload: FieldingOverallUiModel
}>())

const LOAD_BYSEASON_FIELDINGRECORDS = 'LOAD_BYSEASON_FIELDINGRECORDS;';
const LOAD_BYSEASON_FIELDINGRECORDS_SUCCESS = 'LOAD_BYSEASON_FIELDINGRECORDS_SUCCESS;';

export const LoadBySeasonFieldingRecordsAction = createAction(LOAD_BYSEASON_FIELDINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadBySeasonFieldingRecordsSuccessAction = createAction(LOAD_BYSEASON_FIELDINGRECORDS_SUCCESS, props<{
  payload: FieldingOverallUiModel
}>())

const LOAD_BYYEAR_FIELDINGRECORDS = 'LOAD_BYYEAR_FIELDINGRECORDS;';
const LOAD_BYYEAR_FIELDINGRECORDS_SUCCESS = 'LOAD_BYYEAR_FIELDINGRECORDS_SUCCESS;';

export const LoadByYearFieldingRecordsAction = createAction(LOAD_BYYEAR_FIELDINGRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByYearFieldingRecordsSuccessAction = createAction(LOAD_BYYEAR_FIELDINGRECORDS_SUCCESS, props<{
  payload: FieldingOverallUiModel
}>())

