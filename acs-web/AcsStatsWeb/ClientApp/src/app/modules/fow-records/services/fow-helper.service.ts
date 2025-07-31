import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import {FowOverallState} from '../models/app-state';
import {LoadRecordSummariesAction} from '../../../actions/recordsummary.actions';
import {FindRecords} from '../../../models/find-records.model';
import {HttpUrlBuilderService} from '../../../services/http-url-builder.service';
import {HttpParams} from '@angular/common/http';

@Injectable({providedIn: 'root'})
export class FowHelperService {

  public matchTypeLookup: tMapStringToString = {
    overall: "/api/partnershiprecords/overall",
    inningsByInnings: "/api/partnershiprecords/inningsbyinnings",
    inningsByInningsForWicket: "/api/partnershiprecords/inningsbyinningsforwicket",
    byMatch: "/api/partnershiprecords/match",
    bySeries: "/api/partnershiprecords/series",
    byGround: "/api/partnershiprecords/grounds",
    byHostCountry: "/api/partnershiprecords/host",
    byOpposition: "/api/partnershiprecords/opposition",
    byYear: "/api/partnershiprecords/year",
    bySeason: "/api/partnershiprecords/season",
  }

  constructor(private httpUrlBuilderService: HttpUrlBuilderService) {
  }

  getHttpParamsFromFormData(findRecords: FindRecords, partnershipWicket: number): { params: HttpParams } {
    let oldParams = this.httpUrlBuilderService.getHttpParamsFromFormData(findRecords)
    let params: HttpParams = oldParams.params.set('partnershipWicket', partnershipWicket)
    return {params: params}
  }


  loadSummaries(findRecords: FindRecords, fowStore: Store<FowOverallState>) {

    let matchResult = this.httpUrlBuilderService.getMatchResult(findRecords)

    fowStore.dispatch(LoadRecordSummariesAction({
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

  formatHighestScoreForInnings(innings: number, notOut: boolean, score: number | null) {
    return innings == 0 ? '-' : this.formatHighestScore(notOut, score);
  }

}
