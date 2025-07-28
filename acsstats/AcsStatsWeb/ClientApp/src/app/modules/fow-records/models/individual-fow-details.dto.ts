export interface IndividualFowDetailsDto {
  matchId: string
  playerIds: string;
  playerNames: string;
  player1: string;
  player2: string;
  player1Id: number;
  player2Id: number;
  team: string;
  opponents: string;
  runs: number;
  innings: number;
  wicket: number;
  currentScore: number;
  unbroken1: boolean;
  unbroken2: boolean;
  previousScore: number | null;
  previousWicket: number;
  ground: string;
  matchStartDate: string;
  matchTitle: string;
  resultString: string;
}
