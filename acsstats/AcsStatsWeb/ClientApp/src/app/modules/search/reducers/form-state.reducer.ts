import {createReducer, on} from "@ngrx/store";
import {SavePlayerSearchFormAction, SaveScorecardSearchFormAction} from "../actions/search-state.actions";


export const initialFindScorecardState = {};
export const loadScorecardSearchFormReducer = createReducer(
  initialFindScorecardState,
  on(SaveScorecardSearchFormAction, (state, records) => {
    return records.payload;
  })
);

export const loadPlayerSearchFormReducer = createReducer(
  initialFindScorecardState,
  on(SavePlayerSearchFormAction, (state, records) => {
    return records.payload;
  })
);
