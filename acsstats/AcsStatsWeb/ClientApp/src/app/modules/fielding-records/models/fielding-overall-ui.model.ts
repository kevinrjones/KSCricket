import {SqlResultsEnvelope} from '../../../models/sqlresultsenvelope.model';
import {FieldingCareerRecordDto} from './fielding-overall.model';
import {IndividualFieldingDetailsDto} from './individual-fielding-details.dto';

export interface FieldingOverallUiModel {
  sqlResults: SqlResultsEnvelope<FieldingCareerRecordDto[]>,
  sortOrder: number,
  sortDirection: string
}

export interface InningsByInningsUiModel {
  sqlResults: SqlResultsEnvelope<IndividualFieldingDetailsDto[]>,
  sortOrder: number,
  sortDirection: string
}

