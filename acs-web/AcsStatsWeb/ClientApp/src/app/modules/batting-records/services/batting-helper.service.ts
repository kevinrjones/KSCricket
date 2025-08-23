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

  formatHighestScore(notOut: boolean, score: number | null, bat1: number | null, bat2: number | null, notout1: boolean | null, notout2: boolean | null, isSingleInnings: boolean) {
    let s1 = this.formatScoreWithNotOut(score, notOut)
    let s2 = this.formatScoreWithNotOut(bat1, notout1)
    let s3 = this.formatScoreWithNotOut(bat2, notout2)
    if(isSingleInnings)
      return `${s1}`

    return `${s1}(${s2}, ${s3})`
  }

  formatHighestScoreForInnings(innings: number, score: number | null) {
    if (score == null)
      return innings == 0 ? '-' : this.formatHighestScore(false, score, null, null, null, null, false);

    let notOut = Math.trunc(score) < score
    return innings == 0 ? '-' : this.formatHighestScore(notOut, Math.trunc(score), null, null, null, null, false);
  }

  private formatScoreWithNotOut(score: number | null, notOut: boolean | null) {
    if (score === null || score === undefined) return 'dnb'
    return notOut ? `${score}*&nbsp;` : `${score}&nbsp;`;
  }

}
