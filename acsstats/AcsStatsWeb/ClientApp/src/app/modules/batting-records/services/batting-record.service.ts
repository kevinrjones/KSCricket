import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Envelope} from 'src/app/models/envelope';
import {BattingCareerRecordDto} from '../models/batting-overall.model';
import {IndividualBattingDetailsDto} from '../models/individual-batting-details.dto';
import {SqlResultsEnvelope} from '../../../models/sqlresultsenvelope.model';
import {FindRecords} from '../../../models/find-records.model';
import {BattingHelperService} from './batting-helper.service';
import {HttpUrlBuilderService} from '../../../services/http-url-builder.service';

@Injectable({providedIn: 'root'})
export class BattingRecordService {

  constructor(private httpClient: HttpClient
    , private httpUrlBuilderService: HttpUrlBuilderService
    , private battingHelperService: BattingHelperService) {
  }

  getBattingOverall(findBatting: FindRecords): Observable<Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>> {

    const options: { params: HttpParams } | {} = this.httpUrlBuilderService.getHttpParamsFromFormData(findBatting)

    let url = this.httpUrlBuilderService.buildUrl(this.battingHelperService.matchTypeLookup["overall"], findBatting)
    return this.httpClient.get<Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>>(url, options)
  }


  getBattingInningsByInnings(findBatting: FindRecords): Observable<Envelope<SqlResultsEnvelope<IndividualBattingDetailsDto[]>>> {

    const options = this.httpUrlBuilderService.getHttpParamsFromFormData(findBatting)

    let url = this.httpUrlBuilderService.buildUrl(this.battingHelperService.matchTypeLookup["inningsByInnings"], findBatting)
    return this.httpClient.get<Envelope<SqlResultsEnvelope<IndividualBattingDetailsDto[]>>>(url, options)
  }

  getBattingByMatch(findBatting: FindRecords): Observable<Envelope<SqlResultsEnvelope<IndividualBattingDetailsDto[]>>> {

    const options = this.httpUrlBuilderService.getHttpParamsFromFormData(findBatting)

    let url = this.httpUrlBuilderService.buildUrl(this.battingHelperService.matchTypeLookup["byMatch"], findBatting)
    return this.httpClient.get<Envelope<SqlResultsEnvelope<IndividualBattingDetailsDto[]>>>(`${url}`, options)
  }

  getBattingBySeries(findBatting: FindRecords): Observable<Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>> {

    const options = this.httpUrlBuilderService.getHttpParamsFromFormData(findBatting)

    let url = this.httpUrlBuilderService.buildUrl(this.battingHelperService.matchTypeLookup["bySeries"], findBatting)
    return this.httpClient.get<Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>>(`${url}`, options)
  }

  getBattingByGround(findBatting: FindRecords): Observable<Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>> {

    const options = this.httpUrlBuilderService.getHttpParamsFromFormData(findBatting)

    let url = this.httpUrlBuilderService.buildUrl(this.battingHelperService.matchTypeLookup["byGround"], findBatting)
    return this.httpClient.get<Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>>(`${url}`, options)
  }

  getBattingByHostCountry(findBatting: FindRecords): Observable<Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>> {

    const options = this.httpUrlBuilderService.getHttpParamsFromFormData(findBatting)

    let url = this.httpUrlBuilderService.buildUrl(this.battingHelperService.matchTypeLookup["byHostCountry"], findBatting)
    return this.httpClient.get<Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>>(`${url}`, options)
  }

  getBattingByOpposition(findBatting: FindRecords): Observable<Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>> {

    const options = this.httpUrlBuilderService.getHttpParamsFromFormData(findBatting)

    let url = this.httpUrlBuilderService.buildUrl(this.battingHelperService.matchTypeLookup["byOpposition"], findBatting)
    return this.httpClient.get<Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>>(`${url}`, options)
  }

  getBattingByYear(findBatting: FindRecords): Observable<Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>> {

    const options = this.httpUrlBuilderService.getHttpParamsFromFormData(findBatting)

    let url = this.httpUrlBuilderService.buildUrl(this.battingHelperService.matchTypeLookup["byYear"], findBatting)
    return this.httpClient.get<Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>>(`${url}`, options)
  }

  getBattingBySeason(findBatting: FindRecords): Observable<Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>> {

    const options = this.httpUrlBuilderService.getHttpParamsFromFormData(findBatting)

    let url = this.httpUrlBuilderService.buildUrl(this.battingHelperService.matchTypeLookup["bySeason"], findBatting)
    return this.httpClient.get<Envelope<SqlResultsEnvelope<BattingCareerRecordDto[]>>>(`${url}`, options)
  }

}
