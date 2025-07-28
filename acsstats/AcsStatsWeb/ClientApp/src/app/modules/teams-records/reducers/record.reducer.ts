import {createReducer, on} from '@ngrx/store';
import {
  LoadByGroundTeamRecordsAction,
  LoadByGroundTeamRecordsSuccessAction,
  LoadByHighestTargetChasedTeamRecordsAction,
  LoadByHighestTargetChasedTeamRecordsSuccessAction,
  LoadByHostTeamRecordsAction,
  LoadByHostTeamRecordsSuccessAction,
  LoadByInningsExtrasTeamRecordsAction,
  LoadByInningsExtrasTeamRecordsSuccessAction,
  LoadByLowestTargetDefendedTeamRecordsAction,
  LoadByLowestTargetDefendedTeamRecordsSuccessAction,
  LoadByLowestUnreducedTargetDefendedTeamRecordsAction,
  LoadByLowestUnreducedTargetDefendedTeamRecordsSuccessAction,
  LoadByMatchTeamRecordsAction,
  LoadByMatchTeamRecordsSuccessAction,
  LoadByOppositionTeamRecordsAction,
  LoadByOppositionTeamRecordsSuccessAction,
  LoadByResultsTeamRecordsAction,
  LoadByResultsTeamRecordsSuccessAction,
  LoadBySeasonTeamRecordsAction,
  LoadBySeasonTeamRecordsSuccessAction,
  LoadBySeriesTeamRecordsSuccessAction,
  LoadByYearTeamRecordsAction,
  LoadByYearTeamRecordsSuccessAction,
  LoadInnByInnTeamRecordsAction,
  LoadInnByInnTeamRecordsSuccessAction,
  LoadOverallExtrasTeamRecordsAction,
  LoadOverallExtrasTeamRecordsSuccessAction,
  LoadOverallTeamRecordsAction,
  LoadOverallTeamRecordsSuccessAction
} from '../actions/records.actions';
import {LoadBySeriesBowlingRecordsAction} from '../../bowling-records/actions/records.actions';
import {TeamRecordDto} from '../models/team-overall.model';
import {IndividualTeamDetailsDto} from '../models/individual-team-details.dto';
import {MatchResultsDto} from '../models/match-results.dto';
import {OverallExtrasDto} from '../models/overallExtras.dto';
import {ByInningsExtrasDto} from '../models/by.InningsExtras.dto';
import {ByTargetDto} from '../models/by-target.dto';


export const initialTeamOverallRecordState = {
  sqlResults: {data: Array<TeamRecordDto>(), count: 0},
  sortOrder: 4,
  sortDirection: 'desc',
  error: {}
};
export const loadOverallTeamReducer = createReducer(
  initialTeamOverallRecordState,
  on(LoadOverallTeamRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadOverallTeamRecordsAction, (state, records) => {
    return initialTeamOverallRecordState
  })
);

export const initialTeamInnByInnRecordState = {
  sqlResults: {data: Array<IndividualTeamDetailsDto>(), count: 0},
  sortOrder: 4,
  sortDirection: 'desc'
};
export const loadInnByInnTeamReducer = createReducer(
  initialTeamInnByInnRecordState,
  on(LoadInnByInnTeamRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection
    }
  }),
  on(LoadInnByInnTeamRecordsAction, (state, records) => {
    return initialTeamInnByInnRecordState
  })
)

export const loadByMatchTeamReducer = createReducer(
  initialTeamInnByInnRecordState,
  on(LoadByMatchTeamRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection
    }
  }),
  on(LoadByMatchTeamRecordsAction, (state, records) => {
    return initialTeamInnByInnRecordState
  })
)

export const initialByResultsRecordState = {
  sqlResults: {data: Array<MatchResultsDto>(), count: 0},
  sortOrder: 4,
  sortDirection: 'desc'
};
export const loadByResultsTeamReducer = createReducer(
  initialByResultsRecordState,
  on(LoadByResultsTeamRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection
    }
  }),
  on(LoadByResultsTeamRecordsAction, (state, records) => {
    return initialByResultsRecordState
  })
)

export const loadBySeriesTeamReducer = createReducer(
  initialTeamOverallRecordState,
  on(LoadBySeriesTeamRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadBySeriesBowlingRecordsAction, (state, records) => {
    return initialTeamOverallRecordState
  })
);

export const loadByGroundTeamReducer = createReducer(
  initialTeamOverallRecordState,
  on(LoadByGroundTeamRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadByGroundTeamRecordsAction, (state, records) => {
    return initialTeamOverallRecordState
  })
);


export const loadByHostTeamReducer = createReducer(
  initialTeamOverallRecordState,
  on(LoadByHostTeamRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadByHostTeamRecordsAction, (state, records) => {
    return initialTeamOverallRecordState
  })
);

export const loadByOppositionTeamReducer = createReducer(
  initialTeamOverallRecordState,
  on(LoadByOppositionTeamRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadByOppositionTeamRecordsAction, (state, records) => {
    return initialTeamOverallRecordState
  })
);

export const loadByYearTeamReducer = createReducer(
  initialTeamOverallRecordState,
  on(LoadByYearTeamRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadByYearTeamRecordsAction, (state, records) => {
    return initialTeamOverallRecordState
  })
);

export const loadBySeasonTeamReducer = createReducer(
  initialTeamOverallRecordState,
  on(LoadBySeasonTeamRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadBySeasonTeamRecordsAction, (state, records) => {
    return initialTeamOverallRecordState
  })
);

export const initialTeamOverallExtrasRecordState = {
  sqlResults: {data: Array<OverallExtrasDto>(), count: 0},
  sortOrder: 4,
  sortDirection: 'desc',
  error: {}
};

export const loadOverallExtrasTeamReducer = createReducer(
  initialTeamOverallExtrasRecordState,
  on(LoadOverallExtrasTeamRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadOverallExtrasTeamRecordsAction, (state, records) => {
    return initialTeamOverallExtrasRecordState
  })
);

export const initialTeamByInningsExtrasRecordState = {
  sqlResults: {data: Array<ByInningsExtrasDto>(), count: 0},
  sortOrder: 4,
  sortDirection: 'desc',
  error: {}
};

export const loadByInningsExtrasTeamReducer = createReducer(
  initialTeamByInningsExtrasRecordState,
  on(LoadByInningsExtrasTeamRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadByInningsExtrasTeamRecordsAction, (state, records) => {
    return initialTeamByInningsExtrasRecordState
  })
);

export const initialTeamTargetsRecordState = {
  sqlResults: {data: Array<ByTargetDto>(), count: 0},
  sortOrder: 4,
  sortDirection: 'desc',
  error: {}
};

export const loadByHighestTargetChasedTeamReducer = createReducer(
  initialTeamTargetsRecordState,
  on(LoadByHighestTargetChasedTeamRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadByHighestTargetChasedTeamRecordsAction, (state, records) => {
    return initialTeamTargetsRecordState
  })
);

export const loadByLowestTargetDefendedTeamReducer = createReducer(
  initialTeamTargetsRecordState,
  on(LoadByLowestTargetDefendedTeamRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadByLowestTargetDefendedTeamRecordsAction, (state, records) => {
    return initialTeamTargetsRecordState
  })
);

export const loadByLowestUnreducedTargetDefendedTeamReducer = createReducer(
  initialTeamTargetsRecordState,
  on(LoadByLowestUnreducedTargetDefendedTeamRecordsSuccessAction, (state, records) => {
    return {
      sqlResults: records.payload.sqlResults,
      sortOrder: records.payload.sortOrder,
      sortDirection: records.payload.sortDirection,
      error: {}
    }
  }),
  on(LoadByLowestUnreducedTargetDefendedTeamRecordsAction, (state, records) => {
    return initialTeamTargetsRecordState
  })
);

