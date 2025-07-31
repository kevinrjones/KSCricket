import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable, Subscription} from 'rxjs';
import {BowlingOverallUiModel} from '../../models/bowling-overall-ui.model';
import {RecordsSummaryModel} from '../../../../models/records-summary.model';
import {SortOrder} from '../../../../models/sortorder.model';
import {FindRecords} from '../../../../models/find-records.model';
import {ActivatedRoute, Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {BowlingOverallState} from '../../models/app-state';
import {BowlingHelperService} from '../../services/bowling-helper.service';
import {RecordHelperService} from '../../../../services/record-helper.service';
import {IconProp} from '@fortawesome/fontawesome-svg-core';
import {NavigateValues} from '../../../../models/navigate.model';
import {HttpUrlBuilderService} from '../../../../services/http-url-builder.service';

@Component({
    selector: 'app-bowling-aggregate-record-ui',
    templateUrl: './bowling-aggregate-record-ui.component.html',
    styleUrls: ['./bowling-aggregate-record-ui.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class BowlingAggregateRecordUiComponent implements OnInit {

  @Input() bowlingOverallUiMModel$!: Observable<BowlingOverallUiModel>;
  @Input() title!: string;
  @Input() showTeam!: boolean
  @Input() showOpposition!: boolean
  @Input() showCountry!: boolean
  @Input() showGround!: boolean
  @Input() showYear!: boolean
  @Input() showSeason!: boolean
  @Input() searchType!: string

  @Output() dispatch: EventEmitter<{ payload: FindRecords }> = new EventEmitter();

  bowlingSummary$!: Observable<RecordsSummaryModel>;
  sortOrder!: number;
  pageSize!: number;
  pageNumber!: number;
  importedSortOrder = SortOrder;
  venue!: string;
  findBowlingParams!: FindRecords
  count!: number;
  currentPage!: number;
  private sortDirection!: string;
  private bowlInnByInnSub$!: Subscription;
  private bowlingOverallSub$!: Subscription;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private bowlingStore: Store<BowlingOverallState>,
              private bowlingHelperService: BowlingHelperService,
              private httpUrlBuilderService: HttpUrlBuilderService,
              private recordHelperService: RecordHelperService) {
  }

  ngOnDestroy() {
    this.bowlingOverallSub$.unsubscribe();
  }

  ngOnInit(): void {

    this.bowlingSummary$ = this.bowlingStore.select(s => {
      return s.playerRecordSummary;
    })

    this.route.queryParams.subscribe(params => {

      this.findBowlingParams = params as FindRecords

      this.venue = this.recordHelperService.setVenue(this.findBowlingParams.homeVenue.toLowerCase() == 'true',
        this.findBowlingParams.awayVenue.toLowerCase() == 'true',
        this.findBowlingParams.neutralVenue.toLowerCase() == 'true')

      this.dispatch.emit({payload: this.findBowlingParams})
      this.bowlingHelperService.loadSummaries(this.findBowlingParams, this.bowlingStore)

      let pageInfo = this.recordHelperService.getPageInformation(this.findBowlingParams)

      this.pageSize = pageInfo.pageSize
      this.pageNumber = pageInfo.pageNumber

      this.bowlingOverallSub$ = this.bowlingOverallUiMModel$.subscribe(payload => {
        this.sortOrder = payload.sortOrder
        this.sortDirection = payload.sortDirection
        this.count = payload.sqlResults.count;
        this.currentPage = this.recordHelperService.getCurrentPage(this.findBowlingParams)
      })

    });
  }

  sort(newSortOrder: SortOrder) {
    this.recordHelperService.sort(this.sortOrder, newSortOrder, this.sortDirection, this.router)
  }

  getSortClass = (sortOrder: SortOrder): IconProp => this.recordHelperService.getSortClass(sortOrder, this.sortDirection);


  getBb = (syntheicBB: number) => this.bowlingHelperService.getBb(syntheicBB);

  getEcon = (runs: number, balls: number) => this.bowlingHelperService.getEcon(runs, balls);

  gotoPage(navigateValues: NavigateValues) {
    this.recordHelperService.navigateToPage(navigateValues.startRow, navigateValues.pageSize, this.router)
  }

  navigate(startRow: number) {
    this.recordHelperService.navigateToPage(startRow, this.pageSize, this.router)
  }

  getIndex(ndx: number) {
    return ((this.currentPage - 1) * this.pageSize) + ndx + 1
  }

  pageSizeChange($event: number) {
    this.pageSize = $event
  }

  getLabelForMaidens() {
    let matchSubType = this.recordHelperService.getMatchSubType(this.bowlingSummary$)
    if (matchSubType && (matchSubType.toLowerCase() == 'the hundred' || matchSubType.toLowerCase() == "the women's hundred")) return 'Dots'

    return 'Md'
  }

  getMaidensOrDots(maidens: number, dots: number) {
    let matchSubType = this.recordHelperService.getMatchSubType(this.bowlingSummary$)
    if (matchSubType && (matchSubType.toLowerCase() == 'the hundred' || matchSubType.toLowerCase() == "the women's hundred")) return dots
    return maidens
  }

  getMaidensOrDotsSortOrder() {
    let matchSubType = this.recordHelperService.getMatchSubType(this.bowlingSummary$)
    if (matchSubType && (matchSubType.toLowerCase() == 'the hundred' || matchSubType.toLowerCase() == "the women's hundred")) return this.importedSortOrder.dots
    return this.importedSortOrder.maidens
  }

  getLabelFor5wi() {
    let matchType = this.recordHelperService.getMatchType(this.bowlingSummary$)
    return matchType == "Men's T20"
    || matchType == "Men's International T20"
    || matchType == "Women's T20"
    || matchType == "Women's International T20"
    || matchType == "Men's List A"
    || matchType == "Men's ODI"
    || matchType == "Women's List A"
    || matchType == "Women's ODI"
      ? '4wi' : '5wi';
  }

  isNotFirstClass() {
    let matchType = this.recordHelperService.getMatchType(this.bowlingSummary$)
    return matchType == "Men's T20"
      || matchType == "Men's International T20"
      || matchType == "Women's T20"
      || matchType == "Women's International T20"
      || matchType == "Men's List A"
      || matchType == "Men's ODI"
      || matchType == "Women's List A"
      || matchType == "Women's ODI";
  }
}
