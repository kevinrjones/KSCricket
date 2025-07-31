import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Envelope} from '../../../models/envelope';
import {BowlingCareerRecordDto} from '../models/bowling-overall.model';
import {IndividualBowlingDetailsDto} from '../models/individual-bowling-details.dto';
import {FindRecords} from '../../../models/find-records.model';
import {SqlResultsEnvelope} from '../../../models/sqlresultsenvelope.model';
import {BowlingHelperService} from './bowling-helper.service';
import {HttpUrlBuilderService} from '../../../services/http-url-builder.service';

@Injectable({providedIn: 'root'})
export class BowlingRecordService {

  // .set('fivesLimit', findRecords.fivesLimit)
  constructor(private httpClient: HttpClient,
              private httpUrlBuilderService: HttpUrlBuilderService,
              private bowlingHelperService: BowlingHelperService) {
  }

  getBowlingOverall(findBowling: FindRecords): Observable<Envelope<SqlResultsEnvelope<BowlingCareerRecordDto[]>>> {

    const options = this.bowlingHelperService.getHttpParamsFromFormDataForBowling(findBowling)

    let url = this.httpUrlBuilderService.buildUrl(this.bowlingHelperService.matchTypeLookup["overall"], findBowling)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<BowlingCareerRecordDto[]>>>(`${url}`, options)
  }

  getBowlingInningsByInnings(findBowling: FindRecords): Observable<Envelope<SqlResultsEnvelope<IndividualBowlingDetailsDto[]>>> {

    const options: {
      params: HttpParams
    } | {} = this.bowlingHelperService.getHttpParamsFromFormDataForBowling(findBowling)

    let url = this.httpUrlBuilderService.buildUrl(this.bowlingHelperService.matchTypeLookup["inningsByInnings"], findBowling)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<IndividualBowlingDetailsDto[]>>>(`${url}`, options)
  }

  getBowlingByMatch(findBowling: FindRecords): Observable<Envelope<SqlResultsEnvelope<IndividualBowlingDetailsDto[]>>> {

    const options: {
      params: HttpParams
    } | {} = this.bowlingHelperService.getHttpParamsFromFormDataForBowling(findBowling)

    let url = this.httpUrlBuilderService.buildUrl(this.bowlingHelperService.matchTypeLookup["byMatch"], findBowling)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<IndividualBowlingDetailsDto[]>>>(`${url}`, options)
  }

  getBowlingBySeries(findBowling: FindRecords): Observable<Envelope<SqlResultsEnvelope<BowlingCareerRecordDto[]>>> {

    const options: {
      params: HttpParams
    } | {} = this.bowlingHelperService.getHttpParamsFromFormDataForBowling(findBowling)

    let url = this.httpUrlBuilderService.buildUrl(this.bowlingHelperService.matchTypeLookup["bySeries"], findBowling)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<BowlingCareerRecordDto[]>>>(`${url}`, options)
  }

  getBowlingByGround(findBowling: FindRecords): Observable<Envelope<SqlResultsEnvelope<BowlingCareerRecordDto[]>>> {

    const options: {
      params: HttpParams
    } | {} = this.bowlingHelperService.getHttpParamsFromFormDataForBowling(findBowling)

    let url = this.httpUrlBuilderService.buildUrl(this.bowlingHelperService.matchTypeLookup["byGround"], findBowling)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<BowlingCareerRecordDto[]>>>(`${url}`, options)
  }

  getBowlingByHostCountry(findBowling: FindRecords): Observable<Envelope<SqlResultsEnvelope<BowlingCareerRecordDto[]>>> {

    const options: {
      params: HttpParams
    } | {} = this.bowlingHelperService.getHttpParamsFromFormDataForBowling(findBowling)

    let url = this.httpUrlBuilderService.buildUrl(this.bowlingHelperService.matchTypeLookup["byHostCountry"], findBowling)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<BowlingCareerRecordDto[]>>>(`${url}`, options)
  }

  getBowlingByOpposition(findBowling: FindRecords): Observable<Envelope<SqlResultsEnvelope<BowlingCareerRecordDto[]>>> {

    const options: {
      params: HttpParams
    } | {} = this.bowlingHelperService.getHttpParamsFromFormDataForBowling(findBowling)

    let url = this.httpUrlBuilderService.buildUrl(this.bowlingHelperService.matchTypeLookup["byOpposition"], findBowling)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<BowlingCareerRecordDto[]>>>(`${url}`, options)
  }

  getBowlingByYear(findBowling: FindRecords): Observable<Envelope<SqlResultsEnvelope<BowlingCareerRecordDto[]>>> {

    const options: {
      params: HttpParams
    } | {} = this.bowlingHelperService.getHttpParamsFromFormDataForBowling(findBowling)

    let url = this.httpUrlBuilderService.buildUrl(this.bowlingHelperService.matchTypeLookup["byYear"], findBowling)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<BowlingCareerRecordDto[]>>>(`${url}`, options)
  }

  getBowlingBySeason(findBowling: FindRecords): Observable<Envelope<SqlResultsEnvelope<BowlingCareerRecordDto[]>>> {

    const options: {
      params: HttpParams
    } | {} = this.bowlingHelperService.getHttpParamsFromFormDataForBowling(findBowling)

    let url = this.httpUrlBuilderService.buildUrl(this.bowlingHelperService.matchTypeLookup["bySeason"], findBowling)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<BowlingCareerRecordDto[]>>>(`${url}`, options)
  }
}
