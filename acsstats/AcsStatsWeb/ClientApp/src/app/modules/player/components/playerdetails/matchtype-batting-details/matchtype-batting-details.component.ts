import {ChangeDetectionStrategy, Component, Input, OnInit} from '@angular/core';
import {PlayerBattingDetails} from '../../../playerbiography.model';
import {PlayerHelpService} from '../../../services/playerhelp.service';
import {DateTime} from 'luxon';
import {faLink} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-mt-batting-details',
  templateUrl: './matchtype-batting-details.component.html',
  styleUrls: ['./matchtype-batting-details.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class MatchTypeBattingDetailsComponent implements OnInit {

  @Input() matchType!: string
  @Input() playerBattingDetails!: PlayerBattingDetails[];
  @Input() listOfSortedMatchType!: string[];

  previousMatchId: string = "";
  matchNumber: number = 0;
  innings: number = 0;
  protected readonly faLink = faLink;
  private aggregateRuns: number = 0;
  private aggregateNotOuts: number = 0;
  private aggregateBalls: number = 0;
  private aggregateInnings: number = 0;

  constructor(public playerHelpService: PlayerHelpService) {
  }

  ngOnInit(): void {
  }

  getStringValueOrEmptyForMatchId(rowId: string, name: string) {
    return this.previousMatchId == rowId ? '' : name
  }

  getInningsNumber(dismissalType: number): string {
    return dismissalType == 14 ? "-" : this.innings.toString();
  }

  setPreviousMatchId(id: string) {
    this.previousMatchId = id
  }

  getRowClass(id: string, matchType: string): string {
    return this.previousMatchId != id && (matchType == 'f' || matchType == 'wf' || matchType == 't' || matchType == 'wt') ? 'matchRow' : ''
  }

  updateMatchNumber(inningsNumber: number) {
    if (inningsNumber == 1)
      this.matchNumber++
  }

  updateInningsNumber(dismissalType: number) {
    if (dismissalType != 14)
      this.innings++
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
    let score = `${row.score}`

    if (row.dismissalType != 11 && row.dismissalType != 14) {
      return `${score}<span class='position'>${row.position}</span>`
    } else {
      return "-"
    }
  }

  getMatchNumber(row: PlayerBattingDetails) {
    let str = this.getStringValueOrEmptyForMatchId(row.matchId, row.team)
    if (str.length > 0)
      str += `<span class='cwk'>${this.buildCaptainOrWk(row)}</span>`
    return str;
  }

  getHomeTeam(row: PlayerBattingDetails) {
    let str = this.getStringValueOrEmptyForMatchId(row.matchId, row.team)
    if (str.length > 0)
      str += `<span class='cwk'>${this.buildCaptainOrWk(row)}</span>`
    return str;
  }

  getBalls(row: PlayerBattingDetails) {
    if (row.dismissalType == 11 || row.dismissalType == 14) {
      return "-"
    }
    return row.balls == null ? '-' : row.balls
  }

  getMinutes(row: PlayerBattingDetails) {
    if (row.dismissalType == 11 || row.dismissalType == 14) {
      return "-"
    }
    return row.minutes == null ? '-' : row.minutes
  }

  getFours(row: PlayerBattingDetails) {
    if (row.dismissalType == 11 || row.dismissalType == 14) {
      return "-"
    }
    return row.fours == null ? '-' : row.fours
  }

  getSixes(row: PlayerBattingDetails) {
    if (row.dismissalType == 11 || row.dismissalType == 14) {
      return "-"
    }
    return row.sixes == null ? '-' : row.sixes
  }

  getSR(row: PlayerBattingDetails) {
    if (row.dismissalType == 11 || row.dismissalType == 14) {
      return "-"
    }
    return row.sr == null ? '-' : row.sr.toFixed(2)
  }

  getTitle(matchType: string): string {
    return this.playerHelpService.getTitle(matchType)
  }

  asDate(maybeDate: string): string {
    if (maybeDate.length == 0) return ''
    return DateTime.fromISO(maybeDate).toLocaleString(DateTime.DATE_MED)
  }

  getMatchLink(row: PlayerBattingDetails): string {
    return "/scorecard/cardbyid/" + row.matchId
  }

  initialiseRunningTotals() {
    this.matchNumber = 0
    this.innings = 0
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
