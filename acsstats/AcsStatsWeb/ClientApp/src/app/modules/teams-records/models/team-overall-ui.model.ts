import {SqlResultsEnvelope} from '../../../models/sqlresultsenvelope.model';
import {TeamRecordDto} from './team-overall.model';
import {IndividualTeamDetailsDto} from './individual-team-details.dto';
import {MatchResultsDto} from './match-results.dto';
import {OverallExtrasDto} from './overallExtras.dto';
import {ByInningsExtrasDto} from './by.InningsExtras.dto';
import {ByTargetDto} from './by-target.dto';

export interface TeamOverallUiModel {
  sqlResults: SqlResultsEnvelope<TeamRecordDto[]>,
  sortOrder: number,
  sortDirection: string
}

export interface InningsByInningsUiModel {
  sqlResults: SqlResultsEnvelope<IndividualTeamDetailsDto[]>,
  sortOrder: number,
  sortDirection: string
}

export interface ResultsUiModel {
  sqlResults: SqlResultsEnvelope<MatchResultsDto[]>,
  sortOrder: number,
  sortDirection: string
}

export interface OverallExtrasUiModel {
  sqlResults: SqlResultsEnvelope<OverallExtrasDto[]>,
  sortOrder: number,
  sortDirection: string
}

export interface ByInningsExtrasUiModel {
  sqlResults: SqlResultsEnvelope<ByInningsExtrasDto[]>,
  sortOrder: number,
  sortDirection: string
}

export interface ByTargetUiModel {
  sqlResults: SqlResultsEnvelope<ByTargetDto[]>,
  sortOrder: number,
  sortDirection: string
}


