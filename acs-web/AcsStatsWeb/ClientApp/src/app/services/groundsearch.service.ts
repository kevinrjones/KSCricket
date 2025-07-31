import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Envelope} from 'src/app/models/envelope';
import {Ground} from '../models/ground.model';

@Injectable({providedIn: 'root'})
export class GroundSearchService {

  constructor(private httpClient: HttpClient) {
  }

  findGroundsForMatchType(matchType: string): Observable<Envelope<Ground[]>> {
    return this.httpClient.get<Envelope<Ground[]>>(`/api/grounds/${matchType}`)
  }

  findGroundsForMatchTypeAndCountry(matchType: string, countryId: number): Observable<Envelope<Ground[]>> {
    return this.httpClient.get<Envelope<Ground[]>>(`/api/grounds/${matchType}/${countryId}`)
  }


}
