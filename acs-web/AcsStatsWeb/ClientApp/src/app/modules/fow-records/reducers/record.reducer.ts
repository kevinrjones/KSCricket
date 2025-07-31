import {createReducer, on} from '@ngrx/store';
import {FowCareerRecordDto} from '../models/fow-overall.model';
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
  LoadBySeriesFowRecordsSuccessAction,
  LoadByYearFowRecordsAction,
  LoadByYearFowRecordsSuccessAction,
  LoadInnByInnForWicketFowRecordsAction,
  LoadInnByInnFowRecordsAction,
  LoadInnByInnFowRecordsSuccessAction,
  LoadOverallFowRecordsAction,
  LoadOverallFowRecordsSuccessAction
} from '../actions/records.actions';
import {IndividualFowDetailsDto} from '../models/individual-fow-details.dto';
import {LoadBySeriesBowlingRecordsAction} from '../../bowling-records/actions/records.actions';


export const initialFowOverallRecordState = {
  sqlResults: {data: Array<FowCareerRecordDto>(), count: 0},
  sortOrder: 4,
  sortDirection: 'DESC',
  error: {}
};
export const loadOverallFowReducer = createReducer(
  initialFowOverallRecordState,
  on(LoadOverallFowRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadOverallFowRecordsAction, (state, records) => {
    return initialFowOverallRecordState
  })
);

export const initialFowInnByInnRecordState = {
  sqlResults: {data: Array<IndividualFowDetailsDto>(), count: 0},
  sortOrder: 4,
  sortDirection: 'DESC'
};
export const loadInnByInnFowReducer = createReducer(
  initialFowInnByInnRecordState,
  on(LoadInnByInnFowRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection
    }
  }),
  on(LoadInnByInnFowRecordsAction, (state, records) => {
    return initialFowInnByInnRecordState
  }),
  on(LoadInnByInnForWicketFowRecordsAction, (state, records) => {
    return initialFowInnByInnRecordState
  })
)

export const loadByMatchFowReducer = createReducer(
  initialFowInnByInnRecordState,
  on(LoadByMatchFowRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection
    }
  }),
  on(LoadByMatchFowRecordsAction, (state, records) => {
    return initialFowInnByInnRecordState
  })
)

export const loadBySeriesFowReducer = createReducer(
  initialFowOverallRecordState,
  on(LoadBySeriesFowRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadBySeriesBowlingRecordsAction, (state, records) => {
    return initialFowOverallRecordState
  })
);

export const loadByGroundFowReducer = createReducer(
  initialFowOverallRecordState,
  on(LoadByGroundFowRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadByGroundFowRecordsAction, (state, records) => {
    return initialFowOverallRecordState
  })
);


export const loadByHostFowReducer = createReducer(
  initialFowOverallRecordState,
  on(LoadByHostFowRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadByHostFowRecordsAction, (state, records) => {
    return initialFowOverallRecordState
  })
);

export const loadByOppositionFowReducer = createReducer(
  initialFowOverallRecordState,
  on(LoadByOppositionFowRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadByOppositionFowRecordsAction, (state, records) => {
    return initialFowOverallRecordState
  })
);

export const loadByYearFowReducer = createReducer(
  initialFowOverallRecordState,
  on(LoadByYearFowRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadByYearFowRecordsAction, (state, records) => {
    return initialFowOverallRecordState
  })
);

export const loadBySeasonFowReducer = createReducer(
  initialFowOverallRecordState,
  on(LoadBySeasonFowRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadBySeasonFowRecordsAction, (state, records) => {
    return initialFowOverallRecordState
  })
);
