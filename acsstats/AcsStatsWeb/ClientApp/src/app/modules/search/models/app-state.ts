import {FindScorecardState} from "./find-scorecard.models";
import {FindPlayerState} from "./find-player.models";

export interface SearchState {
  search: {
    scorecardSearch: FindScorecardState,
    playerSearch: FindPlayerState
  }
}
