import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Envelope} from 'src/app/models/envelope';
import {Team} from '../models/team.model';

@Injectable({providedIn: 'root'})
export class TeamSearchService {

  constructor(private httpClient: HttpClient) {
  }

  findTeamsForMatchType(matchType: string): Observable<Envelope<Team[]>> {
    return this.httpClient.get<Envelope<Team[]>>(`/api/teams/${matchType}`)
  }

  findTeamsForMatchTypeAndCountry(matchType: string, countryId: number): Observable<Envelope<Team[]>> {
    return this.httpClient.get<Envelope<Team[]>>(`/api/teams/${matchType}/${countryId}`)
  }


}
