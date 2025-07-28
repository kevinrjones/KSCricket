import {Injectable} from '@angular/core';
import {FindRecords} from '../models/find-records.model';
import {HttpParams} from '@angular/common/http';

@Injectable({providedIn: 'root'})
export class HttpUrlBuilderService {

  getMatchResult(findRecords: FindRecords) {
    let matchResult = (findRecords.matchWon == 'true' ? 1 : 0)
      | (findRecords.matchLost == 'true' ? 2 : 0)
      | (findRecords.matchDrawn == 'true' ? 4 : 0)
      | (findRecords.matchTied == 'true' ? 8 : 0)
    return matchResult;
  }

  getHttpParamsFromFormData(findRecords: FindRecords) {
    let venue = findRecords.homeVenue == 'true' ? 1 : 0;
    venue += findRecords.awayVenue == 'true' ? 2 : 0;
    venue += findRecords.neutralVenue == 'true' ? 4 : 0;

    let matchResult = this.getMatchResult(findRecords);

    return findRecords ?
      {

        params: new HttpParams()
          .set('matchSubType', findRecords.matchSubType)
          .set('groundId', findRecords.groundId)
          .set('hostCountryId', findRecords.hostCountryId)
          .set('venue', venue)
          .set('limit', findRecords.limit)
          .set('matchResult', matchResult)
          .set('startDate', findRecords.startDate ?? 0)
          .set('endDate', findRecords.endDate ?? 999999999)
          .set('season', findRecords.season)
          .set('sortOrder', findRecords.sortOrder)
          .set('sortDirection', findRecords.sortDirection)
          .set('startRow', findRecords.startRow)
          .set('pageSize', findRecords.pageSize)
      } : {
        params: new HttpParams()
      };
  }

  buildUrl(baseUrl: string, findRecord: FindRecords): string {
    return `${baseUrl}/${findRecord.matchType}/${findRecord.teamId}/${findRecord.opponentsId}`
  }


  getHttpSearchUrl(baseUrl: string, findRecords: FindRecords) {
    return `${this.buildUrl(baseUrl, findRecords)}/{format}?${this.getHttpParamsFromFormData(findRecords).params.toString()}`
  }

  getHttpSearchUrlEx(baseUrl: string, findRecords: FindRecords, params: String) {
    return `${this.buildUrl(baseUrl, findRecords)}/{format}?${params}`
  }


}
