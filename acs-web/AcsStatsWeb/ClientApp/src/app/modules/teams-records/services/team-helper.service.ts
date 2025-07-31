import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import {LoadRecordSummariesAction} from '../../../actions/recordsummary.actions';
import {FindRecords} from '../../../models/find-records.model';
import {TeamState} from '../models/app-state';
import {HttpUrlBuilderService} from '../../../services/http-url-builder.service';
import {HttpParams} from '@angular/common/http';

@Injectable({providedIn: 'root'})
export class TeamHelperService {

  public matchTypeLookup: tMapStringToString = {
    overall: "/api/teamrecords/overall",
    inningsByInnings: "/api/teamrecords/inningsbyinnings",
    byMatchTotals: "/api/teamrecords/highesttotals",
    byResults: "/api/teamrecords/matchresults",
    bySeries: "/api/teamrecords/series",
    bySeason: "/api/teamrecords/season",
    byGround: "/api/teamrecords/grounds",
    byHostCountry: "/api/teamrecords/host",
    byOpposition: "/api/teamrecords/opposition",
    byYear: "/api/teamrecords/year",
    byOverallExtras: "/api/teamrecords/extras/overall",
    byInningsExtras: "/api/teamrecords/extras/innings",
    byTargetChased: "/api/teamrecords/target/highest",
    byTargetDefended: "/api/teamrecords/target/lowest",
    byUnreducedTargetDefended: "/api/teamrecords/target/lowestunreduced",
  }

  constructor(private httpUrlBuilderService: HttpUrlBuilderService) {
  }

  getHttpParamsFromFormDataForTeams(findRecords: FindRecords): { params: HttpParams } {
    let oldParams = this.httpUrlBuilderService.getHttpParamsFromFormData(findRecords)
    let params: HttpParams = oldParams.params.set('isTeamBattingRecord', findRecords.isTeamBattingRecord)
    return {params: params}
  }


  loadSummaries(findRecords: FindRecords, battingStore: Store<TeamState>) {

    let matchResult = this.httpUrlBuilderService.getMatchResult(findRecords)

    battingStore.dispatch(LoadRecordSummariesAction({
      payload: {
        startDate: findRecords.startDate,
        endDate: findRecords.endDate,
        season: findRecords.season,
        result: matchResult,
        matchSubType: findRecords.matchSubType,
        limit: parseInt(findRecords.limit),
        matchType: findRecords.matchType,
        teamId: findRecords.teamId,
        opponentsId: findRecords.opponentsId,
        groundId: findRecords.groundId,
        hostCountryId: findRecords.hostCountryId
      }
    }))
  }
}
