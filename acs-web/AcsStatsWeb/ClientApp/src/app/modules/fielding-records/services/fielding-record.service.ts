import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Envelope} from 'src/app/models/envelope';
import {FieldingCareerRecordDto} from '../models/fielding-overall.model';
import {IndividualFieldingDetailsDto} from '../models/individual-fielding-details.dto';
import {SqlResultsEnvelope} from '../../../models/sqlresultsenvelope.model';
import {FindRecords} from '../../../models/find-records.model';
import {HttpUrlBuilderService} from '../../../services/http-url-builder.service';
import {FieldingHelperService} from './fielding-helper.service';

@Injectable({providedIn: 'root'})
export class FieldingRecordService {

  constructor(private httpClient: HttpClient,
              private httpUrlBuilderService: HttpUrlBuilderService,
              private fieldingHelperService: FieldingHelperService) {

  }

  getFieldingOverall(findFielding: FindRecords): Observable<Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>> {
    const options: { params: HttpParams } | {} = this.httpUrlBuilderService.getHttpParamsFromFormData(findFielding)

    let url = this.httpUrlBuilderService.buildUrl(this.fieldingHelperService.matchTypeLookup["overall"], findFielding)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>>(`${url}`, options)
  }


  getFieldingInningsByInnings(findFielding: FindRecords): Observable<Envelope<SqlResultsEnvelope<IndividualFieldingDetailsDto[]>>> {
    const options: { params: HttpParams } | {} = this.httpUrlBuilderService.getHttpParamsFromFormData(findFielding)

    let url = this.httpUrlBuilderService.buildUrl(this.fieldingHelperService.matchTypeLookup["inningsByInnings"], findFielding)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<IndividualFieldingDetailsDto[]>>>(`${url}`, options)
  }

  getFieldingByMatch(findFielding: FindRecords): Observable<Envelope<SqlResultsEnvelope<IndividualFieldingDetailsDto[]>>> {

    const options: { params: HttpParams } | {} = this.httpUrlBuilderService.getHttpParamsFromFormData(findFielding)

    let url = this.httpUrlBuilderService.buildUrl(this.fieldingHelperService.matchTypeLookup["byMatch"], findFielding)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<IndividualFieldingDetailsDto[]>>>(`${url}`, options)
  }

  getFieldingBySeries(findFielding: FindRecords): Observable<Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>> {

    const options: { params: HttpParams } | {} = this.httpUrlBuilderService.getHttpParamsFromFormData(findFielding)

    let url = this.httpUrlBuilderService.buildUrl(this.fieldingHelperService.matchTypeLookup["bySeries"], findFielding)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>>(`${url}`, options)
  }

  getFieldingByGround(findFielding: FindRecords): Observable<Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>> {

    const options: { params: HttpParams } | {} = this.httpUrlBuilderService.getHttpParamsFromFormData(findFielding)

    let url = this.httpUrlBuilderService.buildUrl(this.fieldingHelperService.matchTypeLookup["byGround"], findFielding)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>>(`${url}`, options)
  }

  getFieldingByHostCountry(findFielding: FindRecords): Observable<Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>> {

    const options: { params: HttpParams } | {} = this.httpUrlBuilderService.getHttpParamsFromFormData(findFielding)

    let url = this.httpUrlBuilderService.buildUrl(this.fieldingHelperService.matchTypeLookup["byHostCountry"], findFielding)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>>(`${url}`, options)
  }

  getFieldingByOpposition(findFielding: FindRecords): Observable<Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>> {

    const options: { params: HttpParams } | {} = this.httpUrlBuilderService.getHttpParamsFromFormData(findFielding)

    let url = this.httpUrlBuilderService.buildUrl(this.fieldingHelperService.matchTypeLookup["byOpposition"], findFielding)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>>(`${url}`, options)
  }

  getFieldingByYear(findFielding: FindRecords): Observable<Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>> {

    const options: { params: HttpParams } | {} = this.httpUrlBuilderService.getHttpParamsFromFormData(findFielding)

    let url = this.httpUrlBuilderService.buildUrl(this.fieldingHelperService.matchTypeLookup["byYear"], findFielding)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>>(`${url}`, options)
  }

  getFieldingBySeason(findFielding: FindRecords): Observable<Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>> {

    const options: { params: HttpParams } | {} = this.httpUrlBuilderService.getHttpParamsFromFormData(findFielding)

    let url = this.httpUrlBuilderService.buildUrl(this.fieldingHelperService.matchTypeLookup["bySeason"], findFielding)

    return this.httpClient.get<Envelope<SqlResultsEnvelope<FieldingCareerRecordDto[]>>>(`${url}`, options)
  }

}
