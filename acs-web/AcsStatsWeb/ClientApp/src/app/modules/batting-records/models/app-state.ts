import {BattingOverallUiModel, InningsByInningsUiModel} from './batting-overall-ui.model';
import {RecordsSummaryModel} from '../../../models/records-summary.model';

export interface BattingOverallState {
  battingrecords: {
    overall: BattingOverallUiModel,
    inningsByInnings: InningsByInningsUiModel,
    byMatch: InningsByInningsUiModel,
    bySeries: BattingOverallUiModel,
    byGround: BattingOverallUiModel,
    byHost: BattingOverallUiModel,
    byOpposition: BattingOverallUiModel,
    byYear: BattingOverallUiModel,
    bySeason: BattingOverallUiModel,
  },
  playerRecordSummary: RecordsSummaryModel
}

