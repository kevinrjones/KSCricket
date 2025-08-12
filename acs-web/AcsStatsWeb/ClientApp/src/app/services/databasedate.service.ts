import {Injectable} from "@angular/core";
import {Envelope} from "../models/envelope";
import {HttpClient} from "@angular/common/http";
import {MatchDetails} from "../models/matchDetails";
import {MatchDate} from "../models/date.model";
import {SqlResultsEnvelope} from "../models/sqlresultsenvelope.model";

@Injectable({
  providedIn: 'root'
})
export class DatabaseDateService {

  constructor(private httpClient: HttpClient) {
  }

  public getDateOfLastAddedMatch() {
    return this.httpClient.get<Envelope<string>>(`/api/helpers/GetLastDateMatchesAdded`)
  }
}
