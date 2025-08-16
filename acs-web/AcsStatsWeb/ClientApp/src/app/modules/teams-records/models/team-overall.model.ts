export interface TeamRecordDto {
  team: string;
  opponents: string;
  played: number;
  won: number;
  drawn: number;
  lost: number;
  tied: number;
  innings: number;
  totalRuns: number;
  wickets: number;
  avg: number;
  rpo: number;
  hs: number;
  ls: number;
  sr: number;
  seriesDate: string;
  matchStartYear: string;
  ground: string;
  countryName: string;
}

