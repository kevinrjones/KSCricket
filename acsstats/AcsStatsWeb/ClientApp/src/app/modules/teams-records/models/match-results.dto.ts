import {VictoryType} from "../../shared/models/victoryType";


export interface MatchResultsDto {
  matchId: number;
  team: string
  opponents: string;
  victoryType: VictoryType;
  howMuch: number;
  ground: string;
  matchStartDate: string;
  result: number;
  inningsOrder: number;
  resultString: string;
  teamId: number;
  whoWonId: number;
  teamTossId: number;
}
