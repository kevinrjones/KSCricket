import {SqlResultsEnvelope} from '../../../models/sqlresultsenvelope.model';

export interface Player {
  id: number,
  fullName: string,
  teams: string,
  debut: string,
  activeUntil: string
}

export interface PlayerListUiModel {
  sqlResults: SqlResultsEnvelope<Player[]>,
  sortOrder: number,
  sortDirection: string
}
