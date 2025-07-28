import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import {BowlingOverallState} from '../models/app-state';
import {LoadRecordSummariesAction} from '../../../actions/recordsummary.actions';
import {FindRecords} from '../../../models/find-records.model';
import {IndividualBowlingDetailsDto} from '../models/individual-bowling-details.dto';
import {HttpParams} from '@angular/common/http';
import {HttpUrlBuilderService} from '../../../services/http-url-builder.service';

@Injectable({providedIn: 'root'})
export class BowlingHelperService {

  matchTypeLookup: tMapStringToString = {
    overall: "/api/bowlingrecords/overall",
    inningsByInnings: "/api/bowlingrecords/inningsbyinnings",
    byMatch: "/api/bowlingrecords/match",
    bySeries: "/api/bowlingrecords/series",
    byGround: "/api/bowlingrecords/grounds",
    byHostCountry: "/api/bowlingrecords/host",
    byOpposition: "/api/bowlingrecords/opposition",
    byYear: "/api/bowlingrecords/year",
    bySeason: "/api/bowlingrecords/season",
  }

  constructor(private httpUrlBuilderService: HttpUrlBuilderService) {
  }

  getHttpParamsFromFormDataForBowling(findRecords: FindRecords): { params: HttpParams } {
    let oldParams = this.httpUrlBuilderService.getHttpParamsFromFormData(findRecords)
    let params: HttpParams = oldParams.params.set('fivesLimit', findRecords.fivesLimit)
    return {params: params}
  }


  loadSummaries(findRecords: FindRecords, bowlingStore: Store<BowlingOverallState>) {
    let matchResult = this.httpUrlBuilderService.getMatchResult(findRecords)

    bowlingStore.dispatch(LoadRecordSummariesAction({
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

  getOvers(row: IndividualBowlingDetailsDto) {
    if (row.ballsPerOver == 1) return `${row.playerBalls}`
    let oversPart = Math.floor(row.playerBalls / row.ballsPerOver);
    var ballsPart = row.playerBalls % row.ballsPerOver;

    return row.playerBalls == 0 ? '-' : `${oversPart}.${ballsPart}`;
  }

  getBb(syntheticBB: number) {
    let wickets = Math.trunc(syntheticBB)

    let runs = Math.trunc(Math.round(0.1 / (syntheticBB - wickets)))

    return `${wickets}/${runs}`
  }

  getEcon(runs: number, balls: number) {
    let economy = null
    if (balls != null && balls != 0) {
      economy = (runs / balls) * 6;
    }
    return economy != null ? economy.toFixed(2) : '-';
  }

}
