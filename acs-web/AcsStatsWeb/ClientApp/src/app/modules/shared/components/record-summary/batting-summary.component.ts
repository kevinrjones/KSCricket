import {ChangeDetectionStrategy, Component, Input, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {RecordsSummaryModel} from '../../../../models/records-summary.model';

@Component({
    selector: 'app-records-summary',
    templateUrl: './batting-summary.component.html',
    styleUrls: ['./batting-summary.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class RecordSummaryComponent implements OnInit {

  @Input()
  recordSummary$!: Observable<RecordsSummaryModel>;

  recordSummary!: RecordsSummaryModel;

  @Input()
  venue!: string;

  constructor() {
  }

  ngOnInit(): void {
    this.recordSummary$.subscribe(rs => this.recordSummary = rs);
  }

  getTeamDetails() {
    if (this.recordSummary.team == undefined || this.recordSummary.opponents == undefined ||
      (this.recordSummary.team.toLowerCase() == 'all teams' && this.recordSummary.opponents.toLowerCase() == 'all teams')) {
      return `<span class='detail'>All Teams</span>`
    } else {
      // return 'no teams'
      return `<span class='detail'>${this.recordSummary?.team}</span> v <span class='detail'>${this.recordSummary?.opponents}</span>`
    }
  }

  getGroundDetails() {
    if (this.recordSummary.ground == undefined || this.recordSummary.ground.toLowerCase() == 'all grounds') {
      return ''
    } else {
      return ` in <span class='detail'>${this.recordSummary?.ground}</span>`
    }
  }

  getHostDetails() {
    if (this.recordSummary.hostCountry == undefined || this.recordSummary.hostCountry.toLowerCase() == 'all countries') {
      return ''
    } else {
      return ` in <span class='detail'>${this.recordSummary?.hostCountry}</span>`
    }
  }

  getVenueDetails() {
    if (this.venue == undefined || this.venue.toLowerCase() == 'all venues') {
      return ''
    } else {
      return ` at <span class='detail'>${this.venue}</span>`
    }
  }

  getResultDetails() {
    if (this.recordSummary.result == undefined || this.recordSummary.result.toLowerCase() == 'all results') {
      return ''
    } else {
      return ` for <span class='detail'>${this.recordSummary?.result}</span>`
    }
  }

  getDateAndSeason() {
    if (this.recordSummary.season == undefined || this.recordSummary.season.toLowerCase() == 'all seasons') {
      return `<span> between <span class='detail'>${this.recordSummary?.startDate}</span>
              and <span class='detail'>${this.recordSummary?.endDate}</span></span>`
    } else {
      return `<span> in season <span class='detail'>${this.recordSummary?.season}</span>`
    }
  }
}
