import {ChangeDetectionStrategy, Component, Input, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {PlayerOverall} from '../../../playerbiography.model';

@Component({
    selector: 'app-overall-details',
    templateUrl: './overall-details.component.html',
    styleUrls: ['./overall-details.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class OverallDetailsComponent implements OnInit {

  @Input() playerOverall$!: Observable<ReadonlyArray<PlayerOverall[]>>;
  @Input() listOfSortedMatchType!: string[];

  previousMatchId: number = 0;
  hideOverall: boolean = false;
  private matchTypeDictionary: { [matchType: string]: string } =
    {
      'sec': 'Second XI Championship',
      'wf': 'First Class',
      'wt': 'Test',
      'wa': 'List-A',
      'wo': 'ODI',
      'wtt': 'T-20',
      'witt': 'Internation T-20',
      'f': 'First Class',
      't': 'Test',
      'a': 'List-A',
      'o': 'ODI',
      'tt': 'T-20',
      'itt': 'International T-20',
    };

  constructor() {
  }

  ngOnInit(): void {
  }

  setPreviousMatchId(id: number) {
    this.previousMatchId = id
  }

  getStringValueOrEmpty(rowId: number, name: string) {
    return this.previousMatchId == rowId ? '' : name
  }

  getRowClass(id: number, matchType: string) {
    return this.previousMatchId != id && (matchType == 'f' || matchType == 'wf' || matchType == 't' || matchType == 'wt') ? 'class=matchRow' : ''
  }

  totalRowClass(row: PlayerOverall) {
    return row.team === 'Total' ? 'totalRow' : '';
  }

  getBattingAverage(row: PlayerOverall) {
    return row.innings - row.notouts == 0 ? '-' : `${(Math.floor((row.runs * 100) / (row.innings - row.notouts)) / 100).toFixed(2)}`;
  }

  getBowlingAverage(row: PlayerOverall) {
    return row.wickets == 0 || row.bowlingRuns == null || row.wickets == null ? '-' : `${(Math.floor((100 * row.bowlingRuns) / row.wickets) / 100).toFixed(2)}`;
  }

  toMatchTypeDescription(length: number, matchType: string, ndx: number): string {
    if (ndx == 0 || ndx == length - 1) return this.matchTypeDictionary[matchType]
    return ""
  }

  hideOrShow() {
    this.hideOverall = !this.hideOverall
  }

  getHighestScore(highestScore: number): string {
    if (Math.trunc(highestScore) < highestScore)
      return Math.trunc(highestScore) + "*"
    return highestScore.toString()
  }
}
