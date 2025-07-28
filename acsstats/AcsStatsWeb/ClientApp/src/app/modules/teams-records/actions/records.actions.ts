import {createAction, props} from '@ngrx/store';
import {FindRecords} from '../../../models/find-records.model';
import {
  ByInningsExtrasUiModel,
  ByTargetUiModel,
  InningsByInningsUiModel,
  OverallExtrasUiModel,
  ResultsUiModel,
  TeamOverallUiModel
} from '../models/team-overall-ui.model';

const LOAD_OVERALL_TEAMRECORDS = 'LOAD_OVERALL_TEAMRECORDS;';
const LOAD_OVERALL_TEAMRECORDS_SUCCESS = 'LOAD_OVERALL_TEAMRECORDS_SUCCESS;';

export const LoadOverallTeamRecordsAction = createAction(LOAD_OVERALL_TEAMRECORDS, props<{ payload: FindRecords }>())
export const LoadOverallTeamRecordsSuccessAction = createAction(LOAD_OVERALL_TEAMRECORDS_SUCCESS, props<{
  payload: TeamOverallUiModel
}>())

const LOAD_INNBYINN_TEAMRECORDS = 'LOAD_INNBYINN_TEAMRECORDS;';
const LOAD_INNBYINN_TEAMRECORDS_SUCCESS = 'LOAD_INNBYINN_TEAMRECORDS_SUCCESS;';

export const LoadInnByInnTeamRecordsAction = createAction(LOAD_INNBYINN_TEAMRECORDS, props<{ payload: FindRecords }>())
export const LoadInnByInnTeamRecordsSuccessAction = createAction(LOAD_INNBYINN_TEAMRECORDS_SUCCESS, props<{
  payload: InningsByInningsUiModel
}>())

const LOAD_BYMATCH_TEAMRECORDS = 'LOAD_BYMATCH_TEAMRECORDS;';
const LOAD_BYMATCH_TEAMRECORDS_SUCCESS = 'LOAD_BYMATCH_TEAMRECORDS_SUCCESS;';

export const LoadByMatchTeamRecordsAction = createAction(LOAD_BYMATCH_TEAMRECORDS, props<{ payload: FindRecords }>())
export const LoadByMatchTeamRecordsSuccessAction = createAction(LOAD_BYMATCH_TEAMRECORDS_SUCCESS, props<{
  payload: InningsByInningsUiModel
}>())

const LOAD_BYRESULTS_TEAMRECORDS = 'LOAD_BYRESULTS_TEAMRECORDS;';
const LOAD_BYRESULTS_TEAMRECORDS_SUCCESS = 'LOAD_BYRESULTS_TEAMRECORDS_SUCCESS;';

export const LoadByResultsTeamRecordsAction = createAction(LOAD_BYRESULTS_TEAMRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByResultsTeamRecordsSuccessAction = createAction(LOAD_BYRESULTS_TEAMRECORDS_SUCCESS, props<{
  payload: ResultsUiModel
}>())

const LOAD_BYSERIES_TEAMRECORDS = 'LOAD_BYSERIES_TEAMRECORDS;';
const LOAD_BYSERIES_TEAMRECORDS_SUCCESS = 'LOAD_BYSERIES_TEAMRECORDS_SUCCESS;';

export const LoadBySeriesTeamRecordsAction = createAction(LOAD_BYSERIES_TEAMRECORDS, props<{ payload: FindRecords }>())
export const LoadBySeriesTeamRecordsSuccessAction = createAction(LOAD_BYSERIES_TEAMRECORDS_SUCCESS, props<{
  payload: TeamOverallUiModel
}>())

const LOAD_BYGROUND_TEAMRECORDS = 'LOAD_BYGROUND_TEAMRECORDS;';
const LOAD_BYGROUND_TEAMRECORDS_SUCCESS = 'LOAD_BYGROUND_TEAMRECORDS_SUCCESS;';

export const LoadByGroundTeamRecordsAction = createAction(LOAD_BYGROUND_TEAMRECORDS, props<{ payload: FindRecords }>())
export const LoadByGroundTeamRecordsSuccessAction = createAction(LOAD_BYGROUND_TEAMRECORDS_SUCCESS, props<{
  payload: TeamOverallUiModel
}>())

const LOAD_BYHOST_TEAMRECORDS = 'LOAD_BYHOST_TEAMRECORDS;';
const LOAD_BYHOST_TEAMRECORDS_SUCCESS = 'LOAD_BYHOST_TEAMRECORDS_SUCCESS;';

export const LoadByHostTeamRecordsAction = createAction(LOAD_BYHOST_TEAMRECORDS, props<{ payload: FindRecords }>())
export const LoadByHostTeamRecordsSuccessAction = createAction(LOAD_BYHOST_TEAMRECORDS_SUCCESS, props<{
  payload: TeamOverallUiModel
}>())

const LOAD_BYOPPOSITION_TEAMRECORDS = 'LOAD_BYOPPOSITION_TEAMRECORDS;';
const LOAD_BYOPPOSITION_TEAMRECORDS_SUCCESS = 'LOAD_BYOPPOSITION_TEAMRECORDS_SUCCESS;';

export const LoadByOppositionTeamRecordsAction = createAction(LOAD_BYOPPOSITION_TEAMRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByOppositionTeamRecordsSuccessAction = createAction(LOAD_BYOPPOSITION_TEAMRECORDS_SUCCESS, props<{
  payload: TeamOverallUiModel
}>())

const LOAD_BYSEASON_TEAMRECORDS = 'LOAD_BYSEASON_TEAMRECORDS;';
const LOAD_BYSEASON_TEAMRECORDS_SUCCESS = 'LOAD_BYSEASON_TEAMRECORDS_SUCCESS;';

export const LoadBySeasonTeamRecordsAction = createAction(LOAD_BYSEASON_TEAMRECORDS, props<{ payload: FindRecords }>())
export const LoadBySeasonTeamRecordsSuccessAction = createAction(LOAD_BYSEASON_TEAMRECORDS_SUCCESS, props<{
  payload: TeamOverallUiModel
}>())

const LOAD_BYYEAR_TEAMRECORDS = 'LOAD_BYYEAR_TEAMRECORDS;';
const LOAD_BYYEAR_TEAMRECORDS_SUCCESS = 'LOAD_BYYEAR_TEAMRECORDS_SUCCESS;';

export const LoadByYearTeamRecordsAction = createAction(LOAD_BYYEAR_TEAMRECORDS, props<{ payload: FindRecords }>())
export const LoadByYearTeamRecordsSuccessAction = createAction(LOAD_BYYEAR_TEAMRECORDS_SUCCESS, props<{
  payload: TeamOverallUiModel
}>())

const LOAD_EXTRAS_OVERALL_TEAMRECORDS = 'LOAD_EXTRAS_OVERALL_TEAMRECORDS;';
const LOAD_EXTRAS_OVERALL_TEAMRECORDS_SUCCESS = 'LOAD_EXTRAS_OVERALL_TEAMRECORDS_SUCCESS;';

export const LoadOverallExtrasTeamRecordsAction = createAction(LOAD_EXTRAS_OVERALL_TEAMRECORDS, props<{
  payload: FindRecords
}>())
export const LoadOverallExtrasTeamRecordsSuccessAction = createAction(LOAD_EXTRAS_OVERALL_TEAMRECORDS_SUCCESS, props<{
  payload: OverallExtrasUiModel
}>())

const LOAD_EXTRAS_BYINNINGS_TEAMRECORDS = 'LOAD_EXTRAS_BYINNINGS_TEAMRECORDS;';
const LOAD_EXTRAS_BYINNINGS_TEAMRECORDS_SUCCESS = 'LOAD_EXTRAS_BYINNINGS_TEAMRECORDS_SUCCESS;';

export const LoadByInningsExtrasTeamRecordsAction = createAction(LOAD_EXTRAS_BYINNINGS_TEAMRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByInningsExtrasTeamRecordsSuccessAction = createAction(LOAD_EXTRAS_BYINNINGS_TEAMRECORDS_SUCCESS, props<{
  payload: ByInningsExtrasUiModel
}>())

const LOAD_HIGHESTTARGETCHASED_TEAMRECORDS = 'LOAD_HIGHESTTARGETCHASED_TEAMRECORDS;';
const LOAD_HIGHESTTARGETCHASED_TEAMRECORDS_SUCCESS = 'LOAD_HIGHESTTARGETCHASED_TEAMRECORDS_SUCCESS;';

export const LoadByHighestTargetChasedTeamRecordsAction = createAction(LOAD_HIGHESTTARGETCHASED_TEAMRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByHighestTargetChasedTeamRecordsSuccessAction = createAction(LOAD_HIGHESTTARGETCHASED_TEAMRECORDS_SUCCESS, props<{
  payload: ByTargetUiModel
}>())

const LOAD_LOWESTTARGETDEFENDED_TEAMRECORDS = 'LOAD_LOWESTTARGETDEFENDED_TEAMRECORDS;';
const LOAD_LOWESTTARGETDEFENDED_TEAMRECORDS_SUCCESS = 'LOAD_LOWESTTARGETCHASED_TEAMRECORDS_SUCCESS;';

export const LoadByLowestTargetDefendedTeamRecordsAction = createAction(LOAD_LOWESTTARGETDEFENDED_TEAMRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByLowestTargetDefendedTeamRecordsSuccessAction = createAction(LOAD_LOWESTTARGETDEFENDED_TEAMRECORDS_SUCCESS, props<{
  payload: ByTargetUiModel
}>())

const LOAD_LOWESTUNREDUCEDTARGETDEFENDED_TEAMRECORDS = 'LOAD_LOWESTUNREDUCEDTARGETDEFENDED_TEAMRECORDS;';
const LOAD_LOWESTUNREDUCEDTARGETDEFENDED_TEAMRECORDS_SUCCESS = 'LOAD_LOWESTUNREDUCEDTARGETDEFENDED_TEAMRECORDS_SUCCESS;';

export const LoadByLowestUnreducedTargetDefendedTeamRecordsAction = createAction(LOAD_LOWESTUNREDUCEDTARGETDEFENDED_TEAMRECORDS, props<{
  payload: FindRecords
}>())
export const LoadByLowestUnreducedTargetDefendedTeamRecordsSuccessAction = createAction(LOAD_LOWESTUNREDUCEDTARGETDEFENDED_TEAMRECORDS_SUCCESS, props<{
  payload: ByTargetUiModel
}>())

