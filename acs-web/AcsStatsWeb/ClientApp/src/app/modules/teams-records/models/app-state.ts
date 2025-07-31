import {RecordsSummaryModel} from '../../../models/records-summary.model';
import {
  ByInningsExtrasUiModel,
  ByTargetUiModel,
  InningsByInningsUiModel,
  OverallExtrasUiModel,
  ResultsUiModel,
  TeamOverallUiModel
} from './team-overall-ui.model';

export interface TeamState {
  teamrecords: {
    overall: TeamOverallUiModel,
    byResults: ResultsUiModel,
    bySeries: TeamOverallUiModel,
    byGround: TeamOverallUiModel,
    byHost: TeamOverallUiModel,
    byOpposition: TeamOverallUiModel,
    byYear: TeamOverallUiModel,
    bySeason: TeamOverallUiModel,
    inningsByInnings: InningsByInningsUiModel,
    byMatch: InningsByInningsUiModel,
    overallExtras: OverallExtrasUiModel
    byInningsExtras: ByInningsExtrasUiModel
    byHighestTargetChased: ByTargetUiModel
    byLowestTargetDefended: ByTargetUiModel
    byLowestUnreducedTargetDefended: ByTargetUiModel
  },
  playerRecordSummary: RecordsSummaryModel
}

