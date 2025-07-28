import {BowlingCareerRecordDto} from './bowling-overall.model';
import {IndividualBowlingDetailsDto} from './individual-bowling-details.dto';
import {SqlResultsEnvelope} from '../../../models/sqlresultsenvelope.model';

export interface BowlingOverallUiModel {
  sqlResults: SqlResultsEnvelope<BowlingCareerRecordDto[]>,
  sortOrder: number,
  sortDirection: string
}

export interface InningsByInningsUiModel {
  sqlResults: SqlResultsEnvelope<IndividualBowlingDetailsDto[]>,
  sortOrder: number,
  sortDirection: string
}
