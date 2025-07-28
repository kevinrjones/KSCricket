import {ChangeDetectionStrategy, Component, Input, OnInit} from '@angular/core';
import {PlayerBattingDetails} from '../../../playerbiography.model';
import {Observable} from 'rxjs';
import {PlayerHelpService} from '../../../services/playerhelp.service';
import {DateTime} from 'luxon';
import {faLink} from '@fortawesome/free-solid-svg-icons';

@Component({
    selector: 'app-batting-details',
    templateUrl: './batting-details.component.html',
    styleUrls: ['./batting-details.component.css'],
    changeDetection: ChangeDetectionStrategy.Default,
    standalone: false
})
export class BattingDetailsComponent implements OnInit {

  @Input() playerBattingOverall$!: Observable<{ [matchType: string]: PlayerBattingDetails[] }>;
  @Input() listOfSortedMatchType!: string[];

  previousMatchId: string = "";
  hideDetails: boolean[] = [false, false, false, false, false, false, false, false, false, false, false, false, false, false,];
  protected readonly faLink = faLink;
  private aggregateRuns: number = 0;
  private aggregateNotOuts: number = 0;
  private aggregateBalls: number = 0;
  private aggregateInnings: number = 0;

  constructor(public playerHelpService: PlayerHelpService) {
  }

  ngOnInit(): void {
    this.initialiseRunningTotals()
  }

  getStringValueOrEmpty(rowId: string, name: string) {
    return this.previousMatchId == rowId ? '' : name
  }

  setPreviousMatchId(id: string) {
    this.previousMatchId = id
  }

  getRowClass(id: string, matchType: string) {
    return this.previousMatchId != id && (matchType == 'f' || matchType == 'wf' || matchType == 't' || matchType == 'wt' || matchType == 'sec') ? 'matchRow' : ''
  }

  buildCaptainOrWk(row: PlayerBattingDetails) {
    let capOrWk = '';
    if (row.captain != 0) {
      capOrWk += '*';
    }
    if (row.wicketKeeper != 0) {
      capOrWk += '+';
    }
    return capOrWk
  }

  getScore(row: PlayerBattingDetails) {
    if (row.dismissalType != 11 && row.dismissalType != 14) {
      return `${row.score}<span class='position'>${row.position}</span>`
      // return "23"
    } else {
      return '-'
    }
  }

  getHomeTeam(row: PlayerBattingDetails) {
    let str = this.getStringValueOrEmpty(row.matchId, row.team)
    if (str.length > 0)
      str += `<span class='cwk'>${this.buildCaptainOrWk(row)}</span>`
    return str;
  }

  getBalls(row: PlayerBattingDetails) {
    return row.balls == null ? '-' : row.balls
  }

  getMinutes(row: PlayerBattingDetails) {
    return row.minutes == null ? '-' : row.minutes
  }

  getFours(row: PlayerBattingDetails) {
    return row.fours == null ? '-' : row.fours
  }

  getSixes(row: PlayerBattingDetails) {
    return row.sixes == null ? '-' : row.sixes
  }

  getSR(row: PlayerBattingDetails) {
    return row.sr == null ? '-' : row.sr.toFixed(2)
  }

  getTitle(matchType: string): string {
    return this.playerHelpService.getTitle(matchType)
  }

  asDate(maybeDate: string): string {
    if (maybeDate.length == 0) return ''
    return DateTime.fromISO(maybeDate).toLocaleString(DateTime.DATE_MED)
  }

  hideOrShow(ndx: number) {
    if (ndx < this.hideDetails.length)
      this.hideDetails[ndx] = !this.hideDetails[ndx]
    else
      throw "Unexpected index value"
  }

  getShouldHide(ndx: number): boolean {
    if (ndx >= this.hideDetails.length)
      return false
    return this.hideDetails[ndx]
  }

  getMatchLink(row: PlayerBattingDetails): string {
    return "/scorecard/cardbyid/" + row.matchId
  }

  initialiseRunningTotals() {
    this.aggregateRuns = 0
    this.aggregateNotOuts = 0
    this.aggregateInnings = 0
    this.aggregateBalls = 0
  }

  updateRunningTotals(row: PlayerBattingDetails) {
    this.aggregateRuns += row.score
    this.aggregateNotOuts += row.notOut
    if (row.dismissalType != 11 && row.dismissalType != 14 && row.dismissalType != 15)
      this.aggregateInnings++
    this.aggregateBalls += row.balls
  }

  getRollingRuns = () => this.aggregateRuns;
  getRollingBalls = () => this.aggregateBalls;

  getRollingAverage() {
    if (this.aggregateInnings - this.aggregateNotOuts == 0) return "-"
    let avg = this.aggregateRuns / (this.aggregateInnings - this.aggregateNotOuts)
    return (Math.trunc((avg * 100)) / 100).toFixed(2)
  }

  getRollingSR() {
    if (this.aggregateBalls == 0) return "-"
    return (100 * this.aggregateRuns / (this.aggregateBalls)).toFixed(2)
  }

  getRollingBI(matchType: string): string {
    if (!this.isLimitedOverMatchType(matchType)) return "-"
    if (this.aggregateInnings - this.aggregateNotOuts == 0) return "-"
    if (this.aggregateBalls == 0) return "-"
    let avg = this.aggregateRuns / (this.aggregateInnings - this.aggregateNotOuts)
    let rate = (this.aggregateRuns * 100) / this.aggregateBalls
    let bi = Math.sqrt(avg * rate)
    return bi.toFixed(2);
  }

  isLimitedOverMatchType(matchType: String) {
    return matchType == "itt" || matchType == "witt" || matchType == "o" || matchType == "wo"
      || matchType == "tt" || matchType == "wtt" || matchType == "a" || matchType == "wa"
  }
}
