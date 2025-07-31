export interface IndividualFieldingDetailsDto {
  playerId: number;
  matchId: string
  fullName: string
  team: string
  opponents: string
  inningsNumber: number
  ground: string
  matchDate: string;
  dismissals: number;
  wicketKeepingDismissals: number;
  caught: number;
  stumpings: number;
  caughtKeeper: number;
  caughtFielder: number;
}
