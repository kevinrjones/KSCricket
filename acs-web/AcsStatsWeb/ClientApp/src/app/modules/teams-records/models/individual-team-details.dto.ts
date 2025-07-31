export interface IndividualTeamDetailsDto {
  matchId: number
  caId: string
  team: string
  opponents: string;
  innings: number;
  matchTitle: string;
  ground: string;
  matchDate: string;
  resultString: string;
  totalRuns: string;
  totalWickets: string;
  ballsBowled: number;
  ballsPerOver: number;
  rpo: number;
  allOut: boolean;
  declared: boolean;
}

