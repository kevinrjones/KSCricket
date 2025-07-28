import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable, Subscription} from 'rxjs';
import {RecordsSummaryModel} from '../../../../models/records-summary.model';
import {ByInningsExtrasUiModel} from '../../models/team-overall-ui.model';
import {SortOrder} from '../../../../models/sortorder.model';
import {FindRecords} from '../../../../models/find-records.model';
import {ActivatedRoute, Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {TeamState} from '../../models/app-state';
import {TeamHelperService} from '../../services/team-helper.service';
import {RecordHelperService} from '../../../../services/record-helper.service';
import {IconProp} from '@fortawesome/fontawesome-svg-core';
import {DateTime} from 'luxon';
import {NavigateValues} from '../../../../models/navigate.model';
import {faLink} from '@fortawesome/free-solid-svg-icons';
import {ByInningsExtrasDto} from '../../models/by.InningsExtras.dto';

@Component({
    selector: 'app-team-byinningsextras-ui',
    templateUrl: './team-byinningsextras-ui.component.html',
    styleUrls: ['./team-byinningsextras-ui.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class TeamByInningsExtrasUiComponent implements OnInit {

  @Input() extrasUIModel$!: Observable<ByInningsExtrasUiModel>;
  @Input() title!: string;
  @Input() searchType!: string

  @Output() dispatch: EventEmitter<{ payload: FindRecords }> = new EventEmitter();

  faLink = faLink;
  teamSummary$!: Observable<RecordsSummaryModel>;
  sortOrder!: number;
  importedSortOrder = SortOrder;
  pageSize!: number;
  pageNumber!: number;
  venue!: string;
  findTeamParams!: FindRecords;
  count!: number;
  currentPage!: number;
  private sortDirection!: string;
  private teamInnByInnSub$!: Subscription;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private teamStore: Store<TeamState>,
              private teamHelperService: TeamHelperService,
              private recordHelperService: RecordHelperService) {
  }

  ngOnDestroy(): void {
    this.teamInnByInnSub$.unsubscribe()
  }

  ngOnInit(): void {
    this.teamSummary$ = this.teamStore.select(s => {
      return s.playerRecordSummary;
    })

    this.route.queryParams.subscribe(params => {

      this.findTeamParams = params as FindRecords
      let queryParams = this.teamHelperService.getHttpParamsFromFormDataForTeams(this.findTeamParams).params.toString()

      this.venue = this.recordHelperService.setVenue(this.findTeamParams.homeVenue.toLowerCase() == 'true',
        this.findTeamParams.awayVenue.toLowerCase() == 'true',
        this.findTeamParams.neutralVenue.toLowerCase() == 'true')

      this.dispatch.emit({payload: this.findTeamParams})

      this.teamHelperService.loadSummaries(this.findTeamParams, this.teamStore)

      let pageInfo = this.recordHelperService.getPageInformation(this.findTeamParams)

      this.pageSize = pageInfo.pageSize
      this.pageNumber = pageInfo.pageNumber

      this.teamInnByInnSub$ = this.extrasUIModel$.subscribe(payload => {
        this.sortOrder = payload.sortOrder
        this.sortDirection = payload.sortDirection
        this.count = payload.sqlResults.count;
        this.currentPage = this.recordHelperService.getCurrentPage(this.findTeamParams)
      })

    });

  }

  sort(newSortOrder: SortOrder) {
    this.recordHelperService.sort(this.sortOrder, newSortOrder, this.sortDirection, this.router)
  }


  getSortClass(sortOrder: SortOrder): IconProp {
    return this.recordHelperService.getSortClass(sortOrder, this.sortDirection)
  }

  gotoPage(navigateValues: NavigateValues) {
    this.recordHelperService.navigateToPage(navigateValues.startRow, navigateValues.pageSize, this.router)
  }

  navigate(startRow: number) {
    this.recordHelperService.navigateToPage(startRow, this.pageSize, this.router)
  }

  getIndex(ndx: number) {
    return ((this.currentPage - 1) * this.pageSize) + ndx + 1
  }

  getScore(runs: number, wickets: number) {
    if (wickets === 10) {
      return `${runs}`
    }
    return `${runs}/${wickets}`
  }

  formatDate(startDate: string) {
    DateTime.fromISO(startDate).toLocaleString(DateTime.DATETIME_FULL)
  }

  pageSizeChange($event: number) {
    this.pageSize = $event
  }

  getOversHeader() {
    return this.recordHelperService.isTheHundred(this.teamSummary$) ? 'balls' : 'overs'
  }

  getMatchLink(row: ByInningsExtrasDto) {
    return "/scorecard/cardbyid/" + row.matchId
  }

}
