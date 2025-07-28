import {Ground} from './ground.model';

export interface FindFormState {
  matchType: string;
  matchSubType: string;
  teamId: number;
  opponentsId: number;
  groundId: Ground;
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
  isTeamBattingRecord: boolean;
  isForWicket: boolean;
  limit: string;
  startRow: string;
  pageSize: string;
  format: string;
  partnershipWicket: boolean;
}
