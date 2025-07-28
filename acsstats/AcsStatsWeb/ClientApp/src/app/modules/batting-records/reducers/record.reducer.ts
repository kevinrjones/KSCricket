import {createReducer, on} from '@ngrx/store';
import {BattingCareerRecordDto} from '../models/batting-overall.model';
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
  LoadBySeriesBattingRecordsSuccessAction,
  LoadByYearBattingRecordsAction,
  LoadByYearBattingRecordsSuccessAction,
  LoadInnByInnBattingRecordsAction,
  LoadInnByInnBattingRecordsSuccessAction,
  LoadOverallBattingRecordsAction,
  LoadOverallBattingRecordsSuccessAction
} from '../actions/records.actions';
import {IndividualBattingDetailsDto} from '../models/individual-batting-details.dto';
import {LoadBySeriesBowlingRecordsAction} from '../../bowling-records/actions/records.actions';


export const initialBattingOverallRecordState = {
  sqlResults: {data: Array<BattingCareerRecordDto>(), count: 0},
  sortOrder: 4,
  sortDirection: 'DESC',
  error: {}
};
export const loadOverallBattingReducer = createReducer(
  initialBattingOverallRecordState,
  on(LoadOverallBattingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadOverallBattingRecordsAction, (state, records) => {
    return initialBattingOverallRecordState
  })
);

export const initialBattingInnByInnRecordState = {
  sqlResults: {data: Array<IndividualBattingDetailsDto>(), count: 0},
  sortOrder: 4,
  sortDirection: 'DESC'
};
export const loadInnByInnBattingReducer = createReducer(
  initialBattingInnByInnRecordState,
  on(LoadInnByInnBattingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection
    }
  }),
  on(LoadInnByInnBattingRecordsAction, (state, records) => {
    return initialBattingInnByInnRecordState
  })
)

export const loadByMatchBattingReducer = createReducer(
  initialBattingInnByInnRecordState,
  on(LoadByMatchBattingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection
    }
  }),
  on(LoadByMatchBattingRecordsAction, (state, records) => {
    return initialBattingInnByInnRecordState
  })
)

export const loadBySeriesBattingReducer = createReducer(
  initialBattingOverallRecordState,
  on(LoadBySeriesBattingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadBySeriesBowlingRecordsAction, (state, records) => {
    return initialBattingOverallRecordState
  })
);

export const loadByGroundBattingReducer = createReducer(
  initialBattingOverallRecordState,
  on(LoadByGroundBattingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadByGroundBattingRecordsAction, (state, records) => {
    return initialBattingOverallRecordState
  })
);


export const loadByHostBattingReducer = createReducer(
  initialBattingOverallRecordState,
  on(LoadByHostBattingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadByHostBattingRecordsAction, (state, records) => {
    return initialBattingOverallRecordState
  })
);

export const loadByOppositionBattingReducer = createReducer(
  initialBattingOverallRecordState,
  on(LoadByOppositionBattingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadByOppositionBattingRecordsAction, (state, records) => {
    return initialBattingOverallRecordState
  })
);

export const loadByYearBattingReducer = createReducer(
  initialBattingOverallRecordState,
  on(LoadByYearBattingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadByYearBattingRecordsAction, (state, records) => {
    return initialBattingOverallRecordState
  })
);

export const loadBySeasonBattingReducer = createReducer(
  initialBattingOverallRecordState,
  on(LoadBySeasonBattingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadBySeasonBattingRecordsAction, (state, records) => {
    return initialBattingOverallRecordState
  })
);
