import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import {BattingOverallState} from '../models/app-state';
import {LoadRecordSummariesAction} from '../../../actions/recordsummary.actions';
import {FindRecords} from '../../../models/find-records.model';
import {HttpUrlBuilderService} from '../../../services/http-url-builder.service';

@Injectable({providedIn: 'root'})
export class BattingHelperService {

  public matchTypeLookup: tMapStringToString = {
    overall: "/api/battingrecords/overall",
    inningsByInnings: "/api/battingrecords/inningsbyinnings",
    byMatch: "/api/battingrecords/match",
    bySeries: "/api/battingrecords/series",
    byGround: "/api/battingrecords/grounds",
    byHostCountry: "/api/battingrecords/host",
    byOpposition: "/api/battingrecords/opposition",
    byYear: "/api/battingrecords/year",
    bySeason: "/api/battingrecords/season",
  }

  constructor(private httpUrlBuilderService: HttpUrlBuilderService) {
  }

  loadSummaries(findRecords: FindRecords, battingStore: Store<BattingOverallState>) {

    let matchResult = this.httpUrlBuilderService.getMatchResult(findRecords)

    battingStore.dispatch(LoadRecordSummariesAction({
      payload: {
        startDate: findRecords.startDate,
        endDate: findRecords.endDate,
        season: findRecords.season,
        result: matchResult,
        limit: parseInt(findRecords.limit),
        matchType: findRecords.matchType,
        matchSubType: findRecords.matchSubType,
        teamId: findRecords.teamId,
        opponentsId: findRecords.opponentsId,
        groundId: findRecords.groundId,
        hostCountryId: findRecords.hostCountryId
      }
    }))
  }

  formatHighestScore(notOut: boolean, score: number | null) {
    if (score === null) return '<strong>dnb</strong>'
    return notOut ? `${score}*` : `${score}&nbsp;`;
  }

  formatHighestScoreForInnings(innings: number, score: number | null) {
    if (score == null)
      return innings == 0 ? '-' : this.formatHighestScore(false, score);

    let notOut = Math.trunc(score) < score
    return innings == 0 ? '-' : this.formatHighestScore(notOut, Math.trunc(score));
  }

}
