import {FieldingOverallUiModel, InningsByInningsUiModel} from './fielding-overall-ui.model';
import {RecordsSummaryModel} from '../../../models/records-summary.model';

export interface FieldingOverallState {
  fieldingrecords: {
    overall: FieldingOverallUiModel,
    inningsByInnings: InningsByInningsUiModel,
    byMatch: InningsByInningsUiModel,
    bySeries: FieldingOverallUiModel,
    byGround: FieldingOverallUiModel,
    byHost: FieldingOverallUiModel,
    byOpposition: FieldingOverallUiModel,
    byYear: FieldingOverallUiModel,
    bySeason: FieldingOverallUiModel,
  },
  playerRecordSummary: RecordsSummaryModel
}

