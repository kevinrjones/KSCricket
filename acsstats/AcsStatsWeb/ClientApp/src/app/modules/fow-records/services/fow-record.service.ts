import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Envelope} from 'src/app/models/envelope';
import {FowCareerRecordDto} from '../models/fow-overall.model';
import {IndividualFowDetailsDto} from '../models/individual-fow-details.dto';
import {SqlResultsEnvelope} from '../../../models/sqlresultsenvelope.model';
import {FindRecords} from '../../../models/find-records.model';
import {HttpUrlBuilderService} from '../../../services/http-url-builder.service';
import {FowHelperService} from './fow-helper.service';

@Injectable({providedIn: 'root'})
export class FowRecordService {

  constructor(private httpClient: HttpClient
    , private httpUrlBuilderService: HttpUrlBuilderService
    , private fowHelperService: FowHelperService
  ) {
  }

  getFowOverall(findFow: FindRecords): Observable<Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>> {
    const options: { params: HttpParams } | {} = this.fowHelperService.getHttpParamsFromFormData(findFow, 0)

    let url = this.httpUrlBuilderService.buildUrl(this.fowHelperService.matchTypeLookup["overall"], findFow)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>>(`${url}`, options)

  }


  getFowInningsByInnings(findFow: FindRecords): Observable<Envelope<SqlResultsEnvelope<IndividualFowDetailsDto[]>>> {
    const options: { params: HttpParams } | {} = this.fowHelperService.getHttpParamsFromFormData(findFow, 0)

    let url = this.httpUrlBuilderService.buildUrl(this.fowHelperService.matchTypeLookup["inningsByInnings"], findFow)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<IndividualFowDetailsDto[]>>>(`${url}`, options)
  }

  getFowInningsByInningsByWicket(findFow: FindRecords): Observable<Envelope<SqlResultsEnvelope<IndividualFowDetailsDto[]>>> {

    // partnershipWicket is the ID of the field in the UI, this is
    // 100 for all wickets, 101 for 1st etc
    let partnershipWicket = parseInt(findFow.partnershipWicket);

    const options: {
      params: HttpParams
    } | {} = this.fowHelperService.getHttpParamsFromFormData(findFow, partnershipWicket)
    let url
    if (partnershipWicket == 0) {
      url = this.httpUrlBuilderService.buildUrl(this.fowHelperService.matchTypeLookup["inningsByInnings"], findFow)
    } else {
      url = this.httpUrlBuilderService.buildUrl(this.fowHelperService.matchTypeLookup["inningsByInningsForWicket"], findFow)
    }
    return this.httpClient.get<Envelope<SqlResultsEnvelope<IndividualFowDetailsDto[]>>>(`${url}`, options)
  }

  getFowByMatch(findFow: FindRecords): Observable<Envelope<SqlResultsEnvelope<IndividualFowDetailsDto[]>>> {

    const options: { params: HttpParams } | {} = this.fowHelperService.getHttpParamsFromFormData(findFow, 0)

    let url = this.httpUrlBuilderService.buildUrl(this.fowHelperService.matchTypeLookup["byMatch"], findFow)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<IndividualFowDetailsDto[]>>>(`${url}`, options)
  }

  getFowBySeries(findFow: FindRecords): Observable<Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>> {

    const options: { params: HttpParams } | {} = this.fowHelperService.getHttpParamsFromFormData(findFow, 0)

    let url = this.httpUrlBuilderService.buildUrl(this.fowHelperService.matchTypeLookup["bySeries"], findFow)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>>(`${url}`, options)
  }

  getFowByGround(findFow: FindRecords): Observable<Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>> {

    const options: { params: HttpParams } | {} = this.fowHelperService.getHttpParamsFromFormData(findFow, 0)

    let url = this.httpUrlBuilderService.buildUrl(this.fowHelperService.matchTypeLookup["byGround"], findFow)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>>(`${url}`, options)
  }

  getFowByHostCountry(findFow: FindRecords): Observable<Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>> {

    const options: { params: HttpParams } | {} = this.fowHelperService.getHttpParamsFromFormData(findFow, 0)

    let url = this.httpUrlBuilderService.buildUrl(this.fowHelperService.matchTypeLookup["byHostCountry"], findFow)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>>(`${url}`, options)
  }

  getFowByOpposition(findFow: FindRecords): Observable<Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>> {

    const options: { params: HttpParams } | {} = this.fowHelperService.getHttpParamsFromFormData(findFow, 0)

    let url = this.httpUrlBuilderService.buildUrl(this.fowHelperService.matchTypeLookup["byOpposition"], findFow)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>>(`${url}`, options)
  }

  getFowByYear(findFow: FindRecords): Observable<Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>> {

    const options: { params: HttpParams } | {} = this.fowHelperService.getHttpParamsFromFormData(findFow, 0)

    let url = this.httpUrlBuilderService.buildUrl(this.fowHelperService.matchTypeLookup["byYear"], findFow)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>>(`${url}`, options)
  }

  getFowBySeason(findFow: FindRecords): Observable<Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>> {

    const options: { params: HttpParams } | {} = this.fowHelperService.getHttpParamsFromFormData(findFow, 0)

    let url = this.httpUrlBuilderService.buildUrl(this.fowHelperService.matchTypeLookup["bySeason"], findFow)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<FowCareerRecordDto[]>>>(`${url}`, options)
  }

}
