import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import {FieldingOverallState} from '../models/app-state';
import {LoadRecordSummariesAction} from '../../../actions/recordsummary.actions';
import {FindRecords} from '../../../models/find-records.model';
import {HttpUrlBuilderService} from '../../../services/http-url-builder.service';

@Injectable({providedIn: 'root'})
export class FieldingHelperService {

  matchTypeLookup: tMapStringToString = {
    overall: "/api/fieldingrecords/overall",
    inningsByInnings: "/api/fieldingrecords/inningsbyinnings",
    byMatch: "/api/fieldingrecords/match",
    bySeries: "/api/fieldingrecords/series",
    byGround: "/api/fieldingrecords/grounds",
    byHostCountry: "/api/fieldingrecords/host",
    byOpposition: "/api/fieldingrecords/opposition",
    byYear: "/api/fieldingrecords/year",
    bySeason: "/api/fieldingrecords/season",
  }

  constructor(private httpUrlBuilderService: HttpUrlBuilderService) {
  }

  loadSummaries(findRecords: FindRecords, fieldingStore: Store<FieldingOverallState>) {

    let matchResult = this.httpUrlBuilderService.getMatchResult(findRecords)

    fieldingStore.dispatch(LoadRecordSummariesAction({
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
