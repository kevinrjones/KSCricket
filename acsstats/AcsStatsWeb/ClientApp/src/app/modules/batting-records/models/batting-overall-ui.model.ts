import {BattingCareerRecordDto} from './batting-overall.model';
import {IndividualBattingDetailsDto} from './individual-batting-details.dto';
import {SqlResultsEnvelope} from '../../../models/sqlresultsenvelope.model';

export interface BattingOverallUiModel {
  sqlResults: SqlResultsEnvelope<BattingCareerRecordDto[]>,
  sortOrder: number,
  sortDirection: string
}

export interface InningsByInningsUiModel {
  sqlResults: SqlResultsEnvelope<IndividualBattingDetailsDto[]>,
  sortOrder: number,
  sortDirection: string
}

