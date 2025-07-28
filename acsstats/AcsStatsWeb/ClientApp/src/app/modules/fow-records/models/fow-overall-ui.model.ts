import {FowCareerRecordDto} from './fow-overall.model';
import {IndividualFowDetailsDto} from './individual-fow-details.dto';
import {SqlResultsEnvelope} from '../../../models/sqlresultsenvelope.model';

export interface FowOverallUiModel {
  sqlResults: SqlResultsEnvelope<FowCareerRecordDto[]>,
  sortOrder: number,
  sortDirection: string
}

export interface InningsByInningsUiModel {
  sqlResults: SqlResultsEnvelope<IndividualFowDetailsDto[]>,
  sortOrder: number,
  sortDirection: string
}

