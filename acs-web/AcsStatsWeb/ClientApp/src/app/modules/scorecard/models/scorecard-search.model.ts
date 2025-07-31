export interface FindScorecard {
  homeTeam: string;
  homeTeamExactMatch: boolean;
  awayTeam: string;
  awayTeamExactMatch: boolean;
  venue: string;
  startDate: string;
  endDate: string;
  matchType: string;
  matchResult: string;
}
