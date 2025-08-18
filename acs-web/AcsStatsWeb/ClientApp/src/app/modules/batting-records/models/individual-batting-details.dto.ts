export interface IndividualBattingDetailsDto {
  playerId: number;
  matchId: string;
  fullName: string
  sortNamePart: string
  team: string
  opponents: string
  inningsNumber: number
  ground: string
  matchDate: string
  playerScore: number | null
  sr: number
  bat1: number
  notOut1: boolean
  bat2: number
  notOut2: boolean
  notOut: boolean
  position: number
  balls: number
  fours: number
  sixes: number
  minutes: number
}
