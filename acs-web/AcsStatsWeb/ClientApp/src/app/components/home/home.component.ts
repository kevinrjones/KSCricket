import {ChangeDetectionStrategy, Component, ViewEncapsulation} from '@angular/core';
import {AuthenticationService} from '../../services/authentication.service';
import {HomepageService} from "../../services/homepage.service";
import {Observable} from "rxjs";
import {Envelope} from "../../models/envelope";
import {MatchDetails} from "../../models/matchDetails";
import {SqlResultsEnvelope} from "../../models/sqlresultsenvelope.model";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false,
  encapsulation: ViewEncapsulation.None,
})
export class HomeComponent {
  public authenticated$ = this.auth.getIsAuthenticated();
  public anonymous$ = this.auth.getIsAnonymous();
  public matches$: Observable<Envelope<SqlResultsEnvelope<MatchDetails[]>>> = this.homePageService.getMatches();
  // @ts-ignore
  private matches: Envelope<SqlResultsEnvelope<MatchDetails[]>>;

  constructor(private auth: AuthenticationService, private homePageService: HomepageService) {
  }


  ngOnInit() {
    // this.matches$.subscribe(data => this.matches = data)
  }

  buildResultString(row: MatchDetails) {
    if (row.homeOvers2 == undefined) {
      return this.buildSingleInningsRow(row)
    }
    return this.buildMultiInningsRow(row)
  }

  buildSingleInningsRow(row: MatchDetails) {
    return `<span class='teamName'>${row.homeTeam}</span>
    ${this.buildSingleInningsScoreString(row.homeTotal1, row.homeWickets1, row.homeOvers1, row.durationType)}
    vs <span class='teamName'>${row.awayTeam}</span>
    ${this.buildSingleInningsScoreString(row.awayTotal1, row.awayWickets1, row.awayOvers1, row.durationType)}
    <span class="result"> ${row.resultString}</span>
    <span class="matchType">(${this.addMatchType(row.matchType)})</span>`
  }

  buildMultiInningsRow(row: MatchDetails) {
    return `<span class='teamName'>${row.homeTeam}</span>
    ${this.buildMulitInningsScoreString(row.homeTotal1, row.homeWickets1, row.homeDeclared1, true)}
    ${this.buildMulitInningsScoreString(row.homeTotal2, row.homeWickets2, row.homeDeclared2, false)}
    vs <span class='teamName'>${row.awayTeam}</span>
    ${this.buildMulitInningsScoreString(row.awayTotal1, row.awayWickets1, row.awayDeclared1, true)}
    ${this.buildMulitInningsScoreString(row.awayTotal2, row.awayWickets2, row.awayDeclared2, false)}
    <span class="result"> ${row.resultString}</span>
    <span class="matchType">(${this.addMatchType(row.matchType)})</span>`
  }


  private getOversOrBallsString(oversOrBalls: string | undefined, durationType: string) {
    if (durationType.toLowerCase() == "overs")
      return `${oversOrBalls}ov`

    return `${oversOrBalls}b`
  }

  private buildMulitInningsScoreString(total: number, wickets: number, isDeclared: boolean, isFirstInnings: boolean) {
    if (total == undefined) {
      return ""
    }

    let wicketText = wickets == 10 ? "ao" : `-${wickets}`
    wicketText = isDeclared ? wicketText + "d" : wicketText

    return isFirstInnings ? `<span class='teamScore'>${total}${wicketText}</span>` : `and <span class='teamScore'>${total}${wicketText}</span>`
  }

  private buildSingleInningsScoreString(total: number, wickets: number, overs: string | undefined, durationType: string) {
    let wicketText = wickets == 10 ? "ao" : `-${wickets}`
    return `<span class='teamScore'>${total}${wicketText} (${this.getOversOrBallsString(overs, durationType)})</span>`
  }


  private addMatchType(matchType: string) {
    switch (matchType) {
      case 't': return 'test'
      case 'f': return 'fc'
      case 'o': return 'odi'
      case 'a': return 'lista'
      case 'itt': return 'int t20'
      case 'tt': return 't20'
      case 'wt': return "women's test"
      case 'wf': return "women's fc"
      case 'wo': return "women's odi"
      case 'wa': return "women's lista"
      case 'witt': return "women's intt20"
      case 'wtt': return "women's t20"
      case 'minc': return "minor counties"
      case 'mint': return "minor counties trophy"
      case 'mintt': return "minor counties t20"
      case 'sec': return "second xi"
      case 'set': return "second xi trophy"
      case 'sett': return "second xi t20"
      case 'ut': return "under 19's test"
      case 'uo': return "under 19's one-day"
      case 'uitt': return "under 19's t20"
      case 'wuo': return "women's under 19's one-day"
      case 'wuitt': return "women's under 19's t20"
      default: return "other"
    }
  }

}
