import {ScorecardListItem} from './scorecard-list-item.model';
import {Scorecard} from './scorecard.model';


export interface ScorecardState {
  scorecards: {
    scorecards: ScorecardListItem[],
    scorecardTournaments: ScorecardListItem[],
    scorecard: Scorecard,
    decades: { [decade: number]: string[] },
    tournamentsBySeason: string[]
  },
}
