import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Envelope} from 'src/app/models/envelope';
import {MatchDate} from '../models/date.model';

@Injectable({providedIn: 'root'})
export class DatesService {

  constructor(private httpClient: HttpClient) {
  }

  getSeriesDatesForMatchType(matchType: string): Observable<Envelope<string[]>> {
    return this.httpClient.get<Envelope<string[]>>(`/api/matches/seriesdates/${matchType}`)
  }

  getMatchDatesForMatchType(matchType: string): Observable<Envelope<MatchDate[]>> {
    return this.httpClient.get<Envelope<MatchDate[]>>(`/api/matches/dates/${matchType}`)
  }


}
