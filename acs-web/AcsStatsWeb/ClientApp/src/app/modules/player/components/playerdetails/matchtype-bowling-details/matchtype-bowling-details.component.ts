import {ChangeDetectionStrategy, Component, Input, OnInit} from '@angular/core';
import {PlayerBowlingDetails} from '../../../playerbiography.model';
import {PlayerHelpService} from '../../../services/playerhelp.service';
import {DateTime} from 'luxon';
import {faLink} from '@fortawesome/free-solid-svg-icons';
import {DateHelperService} from "../../../../shared/services/dateHelperService";


@Component({
    selector: 'app-mt-bowling-details',
    templateUrl: './matchtype-bowling-details.component.html',
    styleUrls: ['./matchtype-bowling-details.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class MatchTypeBowlingDetailsComponent implements OnInit {

  @Input() matchType!: string
  @Input() playerBowlingDetails!: PlayerBowlingDetails[];
  @Input() listOfSortedMatchType!: string[];

  // one entry for each possible section
  hideDetails: boolean[] = [false, false, false, false, false, false, false, false, false, false, false, false, false, false,];


  previousMatchId: string = "";
  protected readonly faLink = faLink;

  constructor(public playerHelpService: PlayerHelpService,
              public dateHelperService: DateHelperService,) {
  }

  ngOnInit(): void {
  }

  setPreviousMatchId(id: string) {
    this.previousMatchId = id
  }

  getStringValueOrEmpty(rowId: string, name: string) {
    return this.previousMatchId == rowId ? '' : name
  }

  getRowClass(id: string, matchType: string) {
    return this.previousMatchId != id && (matchType == 'f' || matchType == 'wf' || matchType == 't' || matchType == 'wt') ? 'class=matchRow' : ''
  }

  caluclateOvers(balls: number, ballsPerOver: number) {
    let overs = Math.floor(balls / ballsPerOver)
    let ballsLeft = balls % ballsPerOver

    if (balls == 0) return '-'
    return ballsLeft > 0 ? `${overs}.${ballsLeft}` : overs

  }

  getDots(dots: number) {
    return dots == null ? '' : `(${dots})`
  }

  getTitle(matchType: string): string {
    return this.playerHelpService.getTitle(matchType)
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

  getMatchLink(row: PlayerBowlingDetails): string {
    return "/scorecard/cardbyid/" + row.matchId
  }

}
