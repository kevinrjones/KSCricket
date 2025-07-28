import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Envelope} from 'src/app/models/envelope';
import {Team} from '../models/team.model';

@Injectable({providedIn: 'root'})
export class CountrySearchService {

  constructor(private httpClient: HttpClient) {
  }

  findCountriesForMatchType(matchType: string): Observable<Envelope<Team[]>> {
    return this.httpClient.get<Envelope<Team[]>>(`/api/countries/${matchType}`)
  }


}
