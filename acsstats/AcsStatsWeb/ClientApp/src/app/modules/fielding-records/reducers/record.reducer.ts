import {createReducer, on} from '@ngrx/store';
import {FieldingCareerRecordDto} from '../models/fielding-overall.model';
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
  LoadBySeriesFieldingRecordsSuccessAction,
  LoadByYearFieldingRecordsAction,
  LoadByYearFieldingRecordsSuccessAction,
  LoadInnByInnFieldingRecordsAction,
  LoadInnByInnFieldingRecordsSuccessAction,
  LoadOverallFieldingRecordsAction,
  LoadOverallFieldingRecordsSuccessAction
} from '../actions/records.actions';
import {IndividualFieldingDetailsDto} from '../models/individual-fielding-details.dto';
import {LoadBySeriesBowlingRecordsAction} from '../../bowling-records/actions/records.actions';


export const initialFieldingOverallRecordState = {
  sqlResults: {data: Array<FieldingCareerRecordDto>(), count: 0},
  sortOrder: 4,
  sortDirection: 'DESC',
  error: {}
};
export const loadOverallFieldingReducer = createReducer(
  initialFieldingOverallRecordState,
  on(LoadOverallFieldingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadOverallFieldingRecordsAction, (state, records) => {
    return initialFieldingOverallRecordState
  })
);

export const initialFieldingInnByInnRecordState = {
  sqlResults: {data: Array<IndividualFieldingDetailsDto>(), count: 0},
  sortOrder: 4,
  sortDirection: 'DESC'
};
export const loadInnByInnFieldingReducer = createReducer(
  initialFieldingInnByInnRecordState,
  on(LoadInnByInnFieldingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection
    }
  }),
  on(LoadInnByInnFieldingRecordsAction, (state, records) => {
    return initialFieldingInnByInnRecordState
  })
)

export const loadByMatchFieldingReducer = createReducer(
  initialFieldingInnByInnRecordState,
  on(LoadByMatchFieldingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection
    }
  }),
  on(LoadByMatchFieldingRecordsAction, (state, records) => {
    return initialFieldingInnByInnRecordState
  })
)

export const loadBySeriesFieldingReducer = createReducer(
  initialFieldingOverallRecordState,
  on(LoadBySeriesFieldingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadBySeriesBowlingRecordsAction, (state, records) => {
    return initialFieldingOverallRecordState
  })
);

export const loadByGroundFieldingReducer = createReducer(
  initialFieldingOverallRecordState,
  on(LoadByGroundFieldingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadByGroundFieldingRecordsAction, (state, records) => {
    return initialFieldingOverallRecordState
  })
);


export const loadByHostFieldingReducer = createReducer(
  initialFieldingOverallRecordState,
  on(LoadByHostFieldingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadByHostFieldingRecordsAction, (state, records) => {
    return initialFieldingOverallRecordState
  })
);

export const loadByOppositionFieldingReducer = createReducer(
  initialFieldingOverallRecordState,
  on(LoadByOppositionFieldingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadByOppositionFieldingRecordsAction, (state, records) => {
    return initialFieldingOverallRecordState
  })
);

export const loadByYearFieldingReducer = createReducer(
  initialFieldingOverallRecordState,
  on(LoadByYearFieldingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadByYearFieldingRecordsAction, (state, records) => {
    return initialFieldingOverallRecordState
  })
);

export const loadBySeasonFieldingReducer = createReducer(
  initialFieldingOverallRecordState,
  on(LoadBySeasonFieldingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadBySeasonFieldingRecordsAction, (state, records) => {
    return initialFieldingOverallRecordState
  })
);
