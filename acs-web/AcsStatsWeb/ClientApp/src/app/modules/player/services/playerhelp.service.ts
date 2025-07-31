import {Injectable} from '@angular/core';

type tMatchTypeLookup = {
  [key: string]: string
}

@Injectable({providedIn: 'root'})
export class PlayerHelpService {

  private matchTypeLookup: tMatchTypeLookup = {
    t: "Test Matches",
    o: "ODI",
    itt: "International T20",
    f: "First Class",
    a: "List A",
    tt: "T20",
    wt: "Women's Test Matches",
    wo: "Women's ODI",
    witt: "International T20",
    wf: "Women's First Class",
    wa: "Women's List A",
    wtt: "Women's T20",
    sec: "Second XI",
  }

  getTitle(matchType: string): string {
    return this.matchTypeLookup[matchType]
  }

}
