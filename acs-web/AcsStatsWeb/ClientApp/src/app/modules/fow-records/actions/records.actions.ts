import {createAction, props} from '@ngrx/store';
import {FowOverallUiModel, InningsByInningsUiModel} from '../models/fow-overall-ui.model';
import {FindRecords} from '../../../models/find-records.model';

const LOAD_OVERALL_FOWRECORDS = 'LOAD_OVERALL_FOWRECORDS;';
const LOAD_OVERALL_FOWRECORDS_SUCCESS = 'LOAD_OVERALL_FOWRECORDS_SUCCESS;';

export const LoadOverallFowRecordsAction = createAction(LOAD_OVERALL_FOWRECORDS, props<{ payload: FindRecords }>())
export const LoadOverallFowRecordsSuccessAction = createAction(LOAD_OVERALL_FOWRECORDS_SUCCESS, props<{
  payload: FowOverallUiModel
}>())

const LOAD_INNBYINN_FOWRECORDS = 'LOAD_INNBYINN_FOWRECORDS;';
const LOAD_INNBYINN_FOWRECORDS_SUCCESS = 'LOAD_INNBYINN_FOWRECORDS_SUCCESS;';
const LOAD_INNBYINN_BY_WICKET_FOWRECORDS = 'LOAD_INNBYINN_BY_WICKET_FOWRECORDS;';

export const LoadInnByInnFowRecordsAction = createAction(LOAD_INNBYINN_FOWRECORDS, props<{ payload: FindRecords }>())
export const LoadInnByInnFowRecordsSuccessAction = createAction(LOAD_INNBYINN_FOWRECORDS_SUCCESS, props<{
  payload: InningsByInningsUiModel
}>())
export const LoadInnByInnForWicketFowRecordsAction = createAction(LOAD_INNBYINN_BY_WICKET_FOWRECORDS, props<{
  payload: FindRecords
}>())

const LOAD_BYMATCH_FOWRECORDS = 'LOAD_BYMATCH_FOWRECORDS;';
const LOAD_BYMATCH_FOWRECORDS_SUCCESS = 'LOAD_BYMATCH_FOWRECORDS_SUCCESS;';

export const LoadByMatchFowRecordsAction = createAction(LOAD_BYMATCH_FOWRECORDS, props<{ payload: FindRecords }>())
export const LoadByMatchFowRecordsSuccessAction = createAction(LOAD_BYMATCH_FOWRECORDS_SUCCESS, props<{
  payload: InningsByInningsUiModel
}>())

const LOAD_BYSERIES_FOWRECORDS = 'LOAD_BYSERIES_FOWRECORDS;';
const LOAD_BYSERIES_FOWRECORDS_SUCCESS = 'LOAD_BYSERIES_FOWRECORDS_SUCCESS;';

export const LoadBySeriesFowRecordsAction = createAction(LOAD_BYSERIES_FOWRECORDS, props<{ payload: FindRecords }>())
export const LoadBySeriesFowRecordsSuccessAction = createAction(LOAD_BYSERIES_FOWRECORDS_SUCCESS, props<{
  payload: FowOverallUiModel
}>())

const LOAD_BYGROUND_FOWRECORDS = 'LOAD_BYGROUND_FOWRECORDS;';
const LOAD_BYGROUND_FOWRECORDS_SUCCESS = 'LOAD_BYGROUND_FOWRECORDS_SUCCESS;';

export const LoadByGroundFowRecordsAction = createAction(LOAD_BYGROUND_FOWRECORDS, props<{ payload: FindRecords }>())
export const LoadByGroundFowRecordsSuccessAction = createAction(LOAD_BYGROUND_FOWRECORDS_SUCCESS, props<{
  payload: FowOverallUiModel
}>())

const LOAD_BYHOST_FOWRECORDS = 'LOAD_BYHOST_FOWRECORDS;';
const LOAD_BYHOST_FOWRECORDS_SUCCESS = 'LOAD_BYHOST_FOWRECORDS_SUCCESS;';

export const LoadByHostFowRecordsAction = createAction(LOAD_BYHOST_FOWRECORDS, props<{ payload: FindRecords }>())
export const LoadByHostFowRecordsSuccessAction = createAction(LOAD_BYHOST_FOWRECORDS_SUCCESS, props<{
  payload: FowOverallUiModel
}>())

const LOAD_BYOPPOSITION_FOWRECORDS = 'LOAD_BYOPPOSITION_FOWRECORDS;';
const LOAD_BYOPPOSITION_FOWRECORDS_SUCCESS = 'LOAD_BYOPPOSITION_FOWRECORDS_SUCCESS;';

export const LoadByOppositionFowRecordsAction = createAction(LOAD_BYOPPOSITION_FOWRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByOppositionFowRecordsSuccessAction = createAction(LOAD_BYOPPOSITION_FOWRECORDS_SUCCESS, props<{
  payload: FowOverallUiModel
}>())

const LOAD_BYSEASON_FOWRECORDS = 'LOAD_BYSEASON_FOWRECORDS;';
const LOAD_BYSEASON_FOWRECORDS_SUCCESS = 'LOAD_BYSEASON_FOWRECORDS_SUCCESS;';

export const LoadBySeasonFowRecordsAction = createAction(LOAD_BYSEASON_FOWRECORDS, props<{ payload: FindRecords }>())
export const LoadBySeasonFowRecordsSuccessAction = createAction(LOAD_BYSEASON_FOWRECORDS_SUCCESS, props<{
  payload: FowOverallUiModel
}>())

const LOAD_BYYEAR_FOWRECORDS = 'LOAD_BYYEAR_FOWRECORDS;';
const LOAD_BYYEAR_FOWRECORDS_SUCCESS = 'LOAD_BYYEAR_FOWRECORDS_SUCCESS;';

export const LoadByYearFowRecordsAction = createAction(LOAD_BYYEAR_FOWRECORDS, props<{ payload: FindRecords }>())
export const LoadByYearFowRecordsSuccessAction = createAction(LOAD_BYYEAR_FOWRECORDS_SUCCESS, props<{
  payload: FowOverallUiModel
}>())

