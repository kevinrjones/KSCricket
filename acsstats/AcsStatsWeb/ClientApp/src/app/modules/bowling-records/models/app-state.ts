import {RecordsSummaryModel} from '../../../models/records-summary.model';
import {BowlingOverallUiModel, InningsByInningsUiModel} from './bowling-overall-ui.model';

export interface BowlingOverallState {
  bowlingrecords: {
    overall: BowlingOverallUiModel,
    inningsByInnings: InningsByInningsUiModel,
    byMatch: InningsByInningsUiModel,
    bySeries: BowlingOverallUiModel,
    byGround: BowlingOverallUiModel,
    byHost: BowlingOverallUiModel,
    byOpposition: BowlingOverallUiModel,
    byYear: BowlingOverallUiModel,
    bySeason: BowlingOverallUiModel,
  },
  playerRecordSummary: RecordsSummaryModel
}
