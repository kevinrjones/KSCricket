import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Envelope} from 'src/app/models/envelope';
import {SqlResultsEnvelope} from '../../../models/sqlresultsenvelope.model';
import {FindRecords} from '../../../models/find-records.model';
import {TeamRecordDto} from '../models/team-overall.model';
import {IndividualTeamDetailsDto} from '../models/individual-team-details.dto';
import {MatchResultsDto} from '../models/match-results.dto';
import {ByTargetDto} from '../models/by-target.dto';
import {OverallExtrasDto} from '../models/overallExtras.dto';
import {ByInningsExtrasDto} from '../models/by.InningsExtras.dto';
import {HttpUrlBuilderService} from '../../../services/http-url-builder.service';
import {TeamHelperService} from './team-helper.service';

@Injectable({providedIn: 'root'})
export class TeamRecordsService {

  constructor(private httpClient: HttpClient
    , private httpUrlBuilderService: HttpUrlBuilderService
    , private teamHelperService: TeamHelperService) {
  }


  getTeamOverall(findRecords: FindRecords): Observable<Envelope<SqlResultsEnvelope<TeamRecordDto[]>>> {
    const options: { params: HttpParams } | {} = this.teamHelperService.getHttpParamsFromFormDataForTeams(findRecords)

    let url = this.httpUrlBuilderService.buildUrl(this.teamHelperService.matchTypeLookup["overall"], findRecords)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<TeamRecordDto[]>>>(`${url}`, options)
  }

  getTeamInningsByInnings(findRecords: FindRecords): Observable<Envelope<SqlResultsEnvelope<IndividualTeamDetailsDto[]>>> {
    const options: { params: HttpParams } | {} = this.teamHelperService.getHttpParamsFromFormDataForTeams(findRecords)

    let url = this.httpUrlBuilderService.buildUrl(this.teamHelperService.matchTypeLookup["inningsByInnings"], findRecords)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<IndividualTeamDetailsDto[]>>>(`${url}`, options)
  }

  getMatchTotals(findRecords: FindRecords): Observable<Envelope<SqlResultsEnvelope<IndividualTeamDetailsDto[]>>> {
    const options: { params: HttpParams } | {} = this.teamHelperService.getHttpParamsFromFormDataForTeams(findRecords)

    let url = this.httpUrlBuilderService.buildUrl(this.teamHelperService.matchTypeLookup["byMatchTotals"], findRecords)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<IndividualTeamDetailsDto[]>>>(`${url}`, options)
  }

  getMatchResults(findRecords: FindRecords): Observable<Envelope<SqlResultsEnvelope<MatchResultsDto[]>>> {
    const options: { params: HttpParams } | {} = this.teamHelperService.getHttpParamsFromFormDataForTeams(findRecords)

    let url = this.httpUrlBuilderService.buildUrl(this.teamHelperService.matchTypeLookup["byResults"], findRecords)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<MatchResultsDto[]>>>(`${url}`, options)
  }

  getTeamBySeries(findRecords: FindRecords): Observable<Envelope<SqlResultsEnvelope<TeamRecordDto[]>>> {
    const options: { params: HttpParams } | {} = this.teamHelperService.getHttpParamsFromFormDataForTeams(findRecords)

    let url = this.httpUrlBuilderService.buildUrl(this.teamHelperService.matchTypeLookup["bySeries"], findRecords)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<TeamRecordDto[]>>>(`${url}`, options)
  }

  getTeamByGround(findRecords: FindRecords): Observable<Envelope<SqlResultsEnvelope<TeamRecordDto[]>>> {
    const options: { params: HttpParams } | {} = this.teamHelperService.getHttpParamsFromFormDataForTeams(findRecords)

    let url = this.httpUrlBuilderService.buildUrl(this.teamHelperService.matchTypeLookup["byGround"], findRecords)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<TeamRecordDto[]>>>(`${url}`, options)
  }

  getTeamByHostCountry(findRecords: FindRecords): Observable<Envelope<SqlResultsEnvelope<TeamRecordDto[]>>> {
    const options: { params: HttpParams } | {} = this.teamHelperService.getHttpParamsFromFormDataForTeams(findRecords)

    let url = this.httpUrlBuilderService.buildUrl(this.teamHelperService.matchTypeLookup["byHostCountry"], findRecords)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<TeamRecordDto[]>>>(`${url}`, options)
  }

  getTeamByOpposition(findRecords: FindRecords): Observable<Envelope<SqlResultsEnvelope<TeamRecordDto[]>>> {
    const options: { params: HttpParams } | {} = this.teamHelperService.getHttpParamsFromFormDataForTeams(findRecords)

    let url = this.httpUrlBuilderService.buildUrl(this.teamHelperService.matchTypeLookup["byOpposition"], findRecords)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<TeamRecordDto[]>>>(`${url}`, options)
  }

  getTeamByYear(findRecords: FindRecords): Observable<Envelope<SqlResultsEnvelope<TeamRecordDto[]>>> {
    const options: { params: HttpParams } | {} = this.teamHelperService.getHttpParamsFromFormDataForTeams(findRecords)

    let url = this.httpUrlBuilderService.buildUrl(this.teamHelperService.matchTypeLookup["byYear"], findRecords)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<TeamRecordDto[]>>>(`${url}`, options)
  }

  getTeamBySeason(findRecords: FindRecords): Observable<Envelope<SqlResultsEnvelope<TeamRecordDto[]>>> {
    const options: { params: HttpParams } | {} = this.teamHelperService.getHttpParamsFromFormDataForTeams(findRecords)

    let url = this.httpUrlBuilderService.buildUrl(this.teamHelperService.matchTypeLookup["bySeason"], findRecords)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<TeamRecordDto[]>>>(`${url}`, options)
  }

  getOverallExtras(findRecords: FindRecords): Observable<Envelope<SqlResultsEnvelope<OverallExtrasDto[]>>> {
    const options: { params: HttpParams } | {} = this.teamHelperService.getHttpParamsFromFormDataForTeams(findRecords)

    let url = this.httpUrlBuilderService.buildUrl(this.teamHelperService.matchTypeLookup["byOverallExtras"], findRecords)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<OverallExtrasDto[]>>>(`${url}`, options)
  }

  getInningsExtras(findRecords: FindRecords): Observable<Envelope<SqlResultsEnvelope<ByInningsExtrasDto[]>>> {
    const options: { params: HttpParams } | {} = this.teamHelperService.getHttpParamsFromFormDataForTeams(findRecords)

    let url = this.httpUrlBuilderService.buildUrl(this.teamHelperService.matchTypeLookup["byInningsExtras"], findRecords)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<ByInningsExtrasDto[]>>>(`${url}`, options)
  }

  getHighestTargetChased(findRecords: FindRecords): Observable<Envelope<SqlResultsEnvelope<ByTargetDto[]>>> {
    const options: { params: HttpParams } | {} = this.teamHelperService.getHttpParamsFromFormDataForTeams(findRecords)

    let url = this.httpUrlBuilderService.buildUrl(this.teamHelperService.matchTypeLookup["byTargetChased"], findRecords)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<ByTargetDto[]>>>(`${url}`, options)
  }

  getLowestTargetChased(findRecords: FindRecords): Observable<Envelope<SqlResultsEnvelope<ByTargetDto[]>>> {
    const options: { params: HttpParams } | {} = this.teamHelperService.getHttpParamsFromFormDataForTeams(findRecords)

    let url = this.httpUrlBuilderService.buildUrl(this.teamHelperService.matchTypeLookup["byTargetDefended"], findRecords)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<ByTargetDto[]>>>(`${url}`, options)
  }

  getLowestUnreducedTargetChased(findRecords: FindRecords): Observable<Envelope<SqlResultsEnvelope<ByTargetDto[]>>> {
    const options: { params: HttpParams } | {} = this.teamHelperService.getHttpParamsFromFormDataForTeams(findRecords)

    let url = this.httpUrlBuilderService.buildUrl(this.teamHelperService.matchTypeLookup["byUnreducedTargetDefended"], findRecords)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<ByTargetDto[]>>>(`${url}`, options)
  }

}
