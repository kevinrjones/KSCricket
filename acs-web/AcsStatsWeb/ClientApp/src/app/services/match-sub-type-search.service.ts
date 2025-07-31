import {Injectable} from '@angular/core';
import {MatchSubTypeModel} from '../models/match-sub-type.model';
import {Observable, of} from 'rxjs';

@Injectable({providedIn: 'root'})
export class MatchSubTypeSearchService {

  matchSubTypes = new Map<string, MatchSubTypeModel[]>()

  matchSubType = new Map<string, string>()

  constructor() {

    this.matchSubType.set('itt', 'All')
    this.matchSubType.set('witt', 'All')
    this.matchSubType.set('tt', 'All')
    this.matchSubType.set('wtt', 'All')
    this.matchSubType.set('wctt', 'T20 World Cup')
    this.matchSubType.set('bbl', 'Big Bash League')
    this.matchSubType.set('bpl', 'Bangladeshi Premier League')
    this.matchSubType.set('cpl', 'Caribbean Premier League')
    this.matchSubType.set('engtt', 'England Domestic T20')
    this.matchSubType.set('hund', 'The Hundred')
    this.matchSubType.set('ipl', 'Indian Premier League')
    this.matchSubType.set('lpl', 'Lanka Premier League')
    this.matchSubType.set('msl', 'Mzansi Super League')
    this.matchSubType.set('nztt', 'New Zealand Domestic T20')
    this.matchSubType.set('psl', 'Pakistani Super League')
    this.matchSubType.set('wwctt', "Women's T20 World Cup")
    this.matchSubType.set('kia', "Kia Super League")
    this.matchSubType.set('wbbl', "Women's Big Bash League")
    this.matchSubType.set('whund', "Women's Hundred")

    this.matchSubType.set('t', "All")
    this.matchSubType.set('f', "All")
    this.matchSubType.set('o', "All")
    this.matchSubType.set('a', "All")
    this.matchSubType.set('wt', "All")
    this.matchSubType.set('wf', "All")
    this.matchSubType.set('wo', "All")
    this.matchSubType.set('wa', "All")

    this.matchSubType.set('wtc', "World Test Championship")
    this.matchSubType.set('cc', "England County Championship")
    this.matchSubType.set('fran', "South Africa Franchise FC")
    this.matchSubType.set('log', "Zimbabwe Logan Cup FC")
    this.matchSubType.set('ncl', "Bangladesh National Cricket League FC")
    this.matchSubType.set('prem', "Sri Lankan Premier League FC")
    this.matchSubType.set('ps', "New Zealand Plunket Shield FC")
    this.matchSubType.set('qea', "Pakistan Quaid-e-Azam Trophy FC")
    this.matchSubType.set('rt', "India Ranji Trophy FC")
    this.matchSubType.set('wid', "West Indies Shell Shield FC")
    this.matchSubType.set('sacboc', "South Africa Cricket Board Howa Bowl FC")
    this.matchSubType.set('ss', "Australia Sheffield Shield FC")

    this.matchSubType.set('wc', "World Cup")
    this.matchSubType.set('ct', "Champions Trophy")
    this.matchSubType.set('icct', "ICC Trophy (List-A Matches Only)")
    this.matchSubType.set('wcsl', "World Cup Super League")

    this.matchSubType.set('wwc', "Women's World Cup")
    this.matchSubType.set('wch', "ICC Women's Championship")
    this.matchSubTypes.set('t', [
      {key: 't', name: 'All'},
      {key: 'wtc', name: 'World Test Championship'},
    ]);
    this.matchSubTypes.set('o', [
      {key: 'o', name: 'All'},
      {key: 'wc', name: "World Cup"},
      {key: 'ct', name: "Champion's Trophy"},
      {key: 'icct', name: 'ICC Trophy (List A Matches Only)'},
      {key: 'wcsl', name: "ICC World Cup Super League"},
    ]);
    this.matchSubTypes.set('itt', [
      {key: 'itt', name: 'All'},
      {key: 'wctt', name: 'T20 World Cup'},
    ]);
    this.matchSubTypes.set('f', [
      {key: 'f', name: 'All'},
      {key: 'cc', name: 'E - County Championship'},
      {key: 'fran', name: 'SA - Franchise Tournament'},
      {key: 'log', name: 'Z - Logan Cup'},
      {key: 'ncl', name: 'BDESH - National Cricket League (only FC games)'},
      {key: 'prem', name: 'SL - First CLass Tournament'},
      {key: 'ps', name: 'NZ - Plunkett Shield'},
      {key: 'qea', name: 'P - Quaid-e-Azam'},
      {key: 'rt', name: 'I - Ranji Trophy'},
      {key: 'wid', name: 'WI - Shell Shield'},
      {key: 'sacboc', name: 'SA - Howa Bowl'},
      {key: 'ss', name: 'A - Sheffield Shield'},
    ]);
    this.matchSubTypes.set('a', [
      {key: 'a', name: 'All'},
    ]);
    this.matchSubTypes.set('tt', [
      {key: 'tt', name: 'All'},
      {key: 'bbl', name: 'Big Bash League'},
      {key: 'bpl', name: 'Bangladeshi Premier League'},
      {key: 'cpl', name: 'Caribbean Premier League'},
      {key: 'engtt', name: 'England Domestic T20'},
      {key: 'hund', name: 'The Hundred'},
      {key: 'ipl', name: 'Indian Premier League'},
      {key: 'lpl', name: 'Lanka Premier League'},
      {key: 'msl', name: 'Mzansi Super League'},
      {key: 'nztt', name: 'New Zealand Domestic T20'},
      {key: 'psl', name: 'Pakistani Super League'},
      {key: 'wctt', name: 'T20 World Cup'},
    ]);
    this.matchSubTypes.set('wt', [
      {key: 'wt', name: 'All'},
    ]);
    this.matchSubTypes.set('wo', [
      {key: 'a', name: 'All'},
      {key: 'wch', name: "ICC Women's Championship"},
    ]);
    this.matchSubTypes.set('witt', [
      {key: 'witt', name: 'All'},
      {key: 'wwctt', name: "Women's T20 World Cup"},
    ]);
    this.matchSubTypes.set('wf', [
      {key: 'wf', name: 'All'},
    ]);
    this.matchSubTypes.set('wa', [
      {key: 'a', name: 'All'},
      {key: 'wwc', name: "Women's World Cup"},
    ]);
    this.matchSubTypes.set('wtt', [
      {key: 'wtt', name: 'All'},
      {key: 'kia', name: 'Kia Super League'},
      {key: 'wbbl', name: "Women's Big Bash League"},
      {key: 'whund', name: "Women's Hundred"},
      {key: 'wwctt', name: "Women's T20 World Cup"},
    ]);

  }

  getMatchSubTypesForMatchType(matchType: string): Observable<MatchSubTypeModel[]> {
    // @ts-ignore
    return of(this.matchSubTypes.has(matchType) ? this.matchSubTypes.get(matchType) : [])
  }

  getMatchSubType(matchSubType: string): string {
    return (this.matchSubType.has(matchSubType) ? this.matchSubType.get(matchSubType) : '') as string
  }
}
