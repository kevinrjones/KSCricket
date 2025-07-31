import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable, Subscription} from 'rxjs';
import {FowOverallUiModel} from '../../models/fow-overall-ui.model';
import {RecordsSummaryModel} from '../../../../models/records-summary.model';
import {SortOrder} from '../../../../models/sortorder.model';
import {FindRecords} from '../../../../models/find-records.model';
import {ActivatedRoute, Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {FowOverallState} from '../../models/app-state';
import {FowHelperService} from '../../services/fow-helper.service';
import {RecordHelperService} from '../../../../services/record-helper.service';
import {FowCareerRecordDto} from '../../models/fow-overall.model';
import {IconProp} from '@fortawesome/fontawesome-svg-core';
import {NavigateValues} from '../../../../models/navigate.model';

@Component({
    selector: 'app-fow-aggregate-record-ui',
    templateUrl: './fow-aggregate-record-ui.component.html',
    styleUrls: ['./fow-aggregate-record-ui.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class FowAggregateRecordUiComponent implements OnInit {

  @Input() fowUIModel$!: Observable<FowOverallUiModel>;
  @Input() title!: string;
  @Input() showTeam!: boolean
  @Input() showOpposition!: boolean
  @Input() showCountry!: boolean
  @Input() showGround!: boolean
  @Input() showYear!: boolean
  @Input() showSeason!: boolean
  @Input() searchType!: string

  @Output() dispatch: EventEmitter<{ payload: FindRecords }> = new EventEmitter();

  fowSummaryModel$!: Observable<RecordsSummaryModel>;
  sortOrder!: number;
  importedSortOrder = SortOrder;
  venue!: string;
  pageSize!: number;
  pageNumber!: number;
  findFowParams!: FindRecords;
  count!: number;
  currentPage!: number;
  private sortDirection!: string;
  private fowUIModelSub$!: Subscription;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private fowStore: Store<FowOverallState>,
              private fowHelperService: FowHelperService,
              private recordHelperService: RecordHelperService) {
  }

  ngOnDestroy() {
    this.fowUIModelSub$.unsubscribe();
  }

  ngOnInit(): void {
    this.showTeam = true

    this.fowSummaryModel$ = this.fowStore.select(s => {
      return s.playerRecordSummary;
    })

    this.route.queryParams.subscribe(params => {

      this.findFowParams = params as FindRecords

      this.venue = this.recordHelperService.setVenue(this.findFowParams.homeVenue.toLowerCase() == 'true',
        this.findFowParams.awayVenue.toLowerCase() == 'true',
        this.findFowParams.neutralVenue.toLowerCase() == 'true')

      this.dispatch.emit({payload: this.findFowParams})
      this.fowHelperService.loadSummaries(this.findFowParams, this.fowStore)

      let pageInfo = this.recordHelperService.getPageInformation(this.findFowParams)

      this.pageSize = pageInfo.pageSize
      this.pageNumber = pageInfo.pageNumber

      this.fowUIModelSub$ = this.fowUIModel$.subscribe(payload => {
        this.sortOrder = payload.sortOrder
        this.sortDirection = payload.sortDirection

        this.count = payload.sqlResults.count;

        this.currentPage = this.recordHelperService.getCurrentPage(this.findFowParams)
      })

    });
  }

  formatHighestScore(row: FowCareerRecordDto) {
    return this.fowHelperService.formatHighestScoreForInnings(row.innings, row.unbroken, row.highestScore)
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
