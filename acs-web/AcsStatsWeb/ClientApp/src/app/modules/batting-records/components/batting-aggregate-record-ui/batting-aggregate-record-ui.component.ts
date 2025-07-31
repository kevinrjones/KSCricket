import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable, Subscription} from 'rxjs';
import {BattingOverallUiModel} from '../../models/batting-overall-ui.model';
import {RecordsSummaryModel} from '../../../../models/records-summary.model';
import {SortOrder} from '../../../../models/sortorder.model';
import {FindRecords} from '../../../../models/find-records.model';
import {ActivatedRoute, Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {BattingOverallState} from '../../models/app-state';
import {BattingHelperService} from '../../services/batting-helper.service';
import {RecordHelperService} from '../../../../services/record-helper.service';
import {BattingCareerRecordDto} from '../../models/batting-overall.model';
import {IconProp} from '@fortawesome/fontawesome-svg-core';
import {NavigateValues} from '../../../../models/navigate.model';

@Component({
    selector: 'app-batting-aggregate-record-ui',
    templateUrl: './batting-aggregate-record-ui.component.html',
    styleUrls: ['./batting-aggregate-record-ui.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class BattingAggregateRecordUiComponent implements OnInit {

  @Input() battingUIModel$!: Observable<BattingOverallUiModel>;
  @Input() title!: string;
  @Input() showTeam!: boolean
  @Input() showOpposition!: boolean
  @Input() showCountry!: boolean
  @Input() showGround!: boolean
  @Input() showYear!: boolean
  @Input() showSeason!: boolean
  @Input() searchType!: string

  @Output() dispatchLoadEvent: EventEmitter<{ payload: FindRecords }> = new EventEmitter();

  battingSummaryModel$!: Observable<RecordsSummaryModel>;
  sortOrder!: number;
  importedSortOrder = SortOrder;
  venue!: string;
  pageSize!: number;
  pageNumber!: number;
  findBattingParams!: FindRecords;
  count!: number;
  currentPage!: number;
  private sortDirection!: string;
  private battingUIModelSub$!: Subscription;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private battingStore: Store<BattingOverallState>,
              private battingHelperService: BattingHelperService,
              private recordHelperService: RecordHelperService) {
  }

  ngOnDestroy() {
    this.battingUIModelSub$.unsubscribe();
  }

  ngOnInit(): void {
    this.showTeam = true

    this.battingSummaryModel$ = this.battingStore.select(s => {
      return s.playerRecordSummary;
    })

    this.route.queryParams.subscribe(params => {

      this.findBattingParams = params as FindRecords

      this.venue = this.recordHelperService.setVenue(
        this.findBattingParams.homeVenue.toLowerCase() == 'true',
        this.findBattingParams.awayVenue.toLowerCase() == 'true',
        this.findBattingParams.neutralVenue.toLowerCase() == 'true')

      this.dispatchLoadEvent.emit({payload: this.findBattingParams})
      this.battingHelperService.loadSummaries(this.findBattingParams, this.battingStore)

      let pageInfo = this.recordHelperService.getPageInformation(this.findBattingParams)

      this.pageSize = pageInfo.pageSize
      this.pageNumber = pageInfo.pageNumber

      this.battingUIModelSub$ = this.battingUIModel$.subscribe(payload => {
        this.sortOrder = payload.sortOrder
        this.sortDirection = payload.sortDirection

        this.count = payload.sqlResults.count;

        this.currentPage = this.recordHelperService.getCurrentPage(this.findBattingParams)
      })

    });
  }

  formatHighestScore(row: BattingCareerRecordDto) {
    return this.battingHelperService.formatHighestScoreForInnings(row.innings, row.highestScore)
  }

  getStrikeRate(runs: number, balls: number) {
    if (balls == 0) return '-'
    return ((runs / balls) * 100).toFixed(2)
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


  getAverage = (innings: number, notOuts: number, avg: number) => this.recordHelperService.getAverage(innings, notOuts, avg);

  getIndex(ndx: number) {
    return ((this.currentPage - 1) * this.pageSize) + ndx + 1
  }

  getNotOuts(innings: number, notOuts: number): string {
    return innings == 0 ? '-' : notOuts.toString()
  }

  pageSizeChange($event: number) {
    this.pageSize = $event
  }
}
