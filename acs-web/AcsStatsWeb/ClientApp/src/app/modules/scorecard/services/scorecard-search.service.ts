import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Envelope} from 'src/app/models/envelope';
import {FindScorecard} from '../models/scorecard-search.model';
import {ScorecardListItem} from '../models/scorecard-list-item.model';
import {Scorecard} from '../models/scorecard.model';

@Injectable({providedIn: 'root'})
export class ScorecardSearchService {

  constructor(private httpClient: HttpClient) {
  }

  findMatches(findScorecard: FindScorecard): Observable<Envelope<ScorecardListItem[]>> {

    const options = findScorecard ?
      {
        params: new HttpParams().set('homeTeam', findScorecard.homeTeam)
          .set('awayTeam', findScorecard.awayTeam)
          .set('homeTeamExactMatch', findScorecard.homeTeamExactMatch)
          .set('awayTeamExactMatch', findScorecard.awayTeamExactMatch)
          .set('startDate', findScorecard.startDate)
          .set('endDate', findScorecard.endDate)
          .set('venue', findScorecard.venue)
          .set('matchResult', findScorecard.matchResult)
          .set('matchType', findScorecard.matchType)
      } : {};

    return this.httpClient.get<Envelope<ScorecardListItem[]>>(`/api/matches/findmatches`, options)
  }

  findTournament(name: string): Observable<Envelope<ScorecardListItem[]>> {
    return this.httpClient.get<Envelope<ScorecardListItem[]>>(`/api/matches/matchesintournament/${encodeURIComponent(name)}`)
  }

  findCard(name: string): Observable<Envelope<Scorecard>> {
    return this.httpClient.get<Envelope<Scorecard>>(`/api/scorecard/${encodeURIComponent(name)}`)
  }

  findCardById(id: string): Observable<Envelope<Scorecard>> {
    return this.httpClient.get<Envelope<Scorecard>>(`/api/scorecard/byid/${id}`)
  }

  findByDecade(type: string): Observable<Envelope<{ [matchType: string]: string[] }>> {

    let matchTypes = type == 'men' ? 'matchtypes=t&matchtypes=f&matchtypes=o&matchtypes=a&matchtypes=tt&matchtypes=itt&matchtypes=sec&matchtypes=set&matchtypes=sett&matchtypes=minc&matchtypes=mint&matchtypes=mintt' :
      'matchtypes=wt&matchtypes=wf&matchtypes=wo&matchtypes=wa&matchtypes=wtt&matchtypes=witt';
    return this.httpClient.get<Envelope<{
      [matchType: string]: string[]
    }>>(`/api/matches/seriesdatesformatchtypes?${matchTypes}`)
  }

  findByYear(season: string, type: string): Observable<Envelope<string[]>> {
    let matchTypes = type == 'men' ? 'matchtypes=t&matchtypes=f&matchtypes=o&matchtypes=a&matchtypes=tt&matchtypes=itt' :
      'matchtypes=wt&matchtypes=wf&matchtypes=wo&matchtypes=wa&matchtypes=wtt&matchtypes=witt';
    return this.httpClient.get<Envelope<string[]>>(`/api/matches/tournamentsforseason/${season}?${matchTypes}`)
  }
}
