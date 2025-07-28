import {FowOverallUiModel, InningsByInningsUiModel} from './fow-overall-ui.model';
import {RecordsSummaryModel} from '../../../models/records-summary.model';

export interface FowOverallState {
  fowrecords: {
    overall: FowOverallUiModel,
    inningsByInnings: InningsByInningsUiModel,
    byMatch: InningsByInningsUiModel,
    bySeries: FowOverallUiModel,
    byGround: FowOverallUiModel,
    byHost: FowOverallUiModel,
    byOpposition: FowOverallUiModel,
    byYear: FowOverallUiModel,
    bySeason: FowOverallUiModel,
  },
  playerRecordSummary: RecordsSummaryModel
}

