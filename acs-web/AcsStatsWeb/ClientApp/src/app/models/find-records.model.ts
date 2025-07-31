export interface FindRecords {
  matchType: string;
  matchSubType: string;
  teamId: number;
  opponentsId: number;
  groundId: number;
  hostCountryId: number;
  homeVenue: string;
  awayVenue: string;
  neutralVenue: string;
  sortOrder: number;
  sortDirection: string;
  startDate: string;
  endDate: string;
  season: string;
  matchWon: string;
  matchLost: string;
  matchDrawn: string;
  matchTied: string;
  fivesLimit: string;
  limit: string;
  isTeamBattingRecord: boolean;
  startRow: string;
  pageSize: string;
  format: string;
  partnershipWicket: string;
}


export interface InitialFormState {
  matchType: string;
}

export interface FormState {
  matchType: string;
  matchSubType: string;
  teamId: number;
  opponentsId: number;
  groundId: number;
  hostCountryId: number;
  homeVenue: string;
  awayVenue: string;
  neutralVenue: string;
  startDate: string;
  endDate: string;
  season: string;
  matchWon: number;
  matchLost: number;
  matchDrawn: number;
  matchTied: number;
  limit: number;
  startRow: string;
  pageSize: number;
  format: number;
  isTeamBattingRecord: boolean;
}
