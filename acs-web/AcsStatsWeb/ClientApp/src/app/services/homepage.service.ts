import {Injectable} from "@angular/core";
import {Envelope} from "../models/envelope";
import {HttpClient} from "@angular/common/http";
import {MatchDetails} from "../models/matchDetails";
import {MatchDate} from "../models/date.model";
import {SqlResultsEnvelope} from "../models/sqlresultsenvelope.model";

@Injectable({
  providedIn: 'root'
})
export class HomepageService {

  constructor(private httpClient: HttpClient) {
  }

  public getMatches() {
    return this.httpClient.get<Envelope<SqlResultsEnvelope<MatchDetails[]>>>(`/api/frontpage/getlatestmatches`)
  }
}
