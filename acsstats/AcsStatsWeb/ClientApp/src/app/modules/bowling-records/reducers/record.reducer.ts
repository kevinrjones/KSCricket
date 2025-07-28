import {createReducer, on} from '@ngrx/store';
import {BowlingCareerRecordDto} from '../models/bowling-overall.model';
import {
  LoadByGroundBowlingRecordsSuccessAction,
  LoadByHostBowlingRecordsSuccessAction,
  LoadByMatchBowlingRecordsSuccessAction,
  LoadByOppositionBowlingRecordsSuccessAction,
  LoadBySeasonBowlingRecordsSuccessAction,
  LoadBySeriesBowlingRecordsSuccessAction,
  LoadByYearBowlingRecordsSuccessAction,
  LoadInnByInnBowlingRecordsSuccessAction,
  LoadOverallBowlingRecordsSuccessAction
} from '../actions/records.actions';
import {IndividualBowlingDetailsDto} from '../models/individual-bowling-details.dto';
import {SortOrder} from 'src/app/models/sortorder.model';


export const initialBowlingOverallRecordState = {
  sqlResults: {data: Array<BowlingCareerRecordDto>(), count: 0},
  sortOrder: SortOrder.wickets,
  sortDirection: 'DESC',
  error: {}
};
export const loadOverallBowlingReducer = createReducer(
  initialBowlingOverallRecordState,
  on(LoadOverallBowlingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  })
);

export const initialBowlingInnByInnRecordState = {
  sqlResults: {data: Array<IndividualBowlingDetailsDto>(), count: 0},
  sortOrder: SortOrder.wickets,
  sortDirection: 'DESC'
};
export const loadInnByInnBowlingReducer = createReducer(
  initialBowlingInnByInnRecordState,
  on(LoadInnByInnBowlingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  })
)

export const loadByMatchBowlingReducer = createReducer(
  initialBowlingInnByInnRecordState,
  on(LoadByMatchBowlingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  })
)

export const loadBySeriesBowlingReducer = createReducer(
  initialBowlingOverallRecordState,
  on(LoadBySeriesBowlingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  })
);

export const loadByGroundBowlingReducer = createReducer(
  initialBowlingOverallRecordState,
  on(LoadByGroundBowlingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  })
);


export const loadByHostBowlingReducer = createReducer(
  initialBowlingOverallRecordState,
  on(LoadByHostBowlingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  })
);

export const loadByOppositionBowlingReducer = createReducer(
  initialBowlingOverallRecordState,
  on(LoadByOppositionBowlingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  })
);

export const loadByYearBowlingReducer = createReducer(
  initialBowlingOverallRecordState,
  on(LoadByYearBowlingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  })
);

export const loadBySeasonBowlingReducer = createReducer(
  initialBowlingOverallRecordState,
  on(LoadBySeasonBowlingRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  })
);
