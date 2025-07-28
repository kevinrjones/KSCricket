import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable, Subscription} from 'rxjs';
import {RecordsSummaryModel} from '../../../../models/records-summary.model';
import {InningsByInningsUiModel} from '../../models/bowling-overall-ui.model';
import {SortOrder} from '../../../../models/sortorder.model';
import {FindRecords} from '../../../../models/find-records.model';
import {ActivatedRoute, Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {BowlingOverallState} from '../../models/app-state';
import {BowlingHelperService} from '../../services/bowling-helper.service';
import {RecordHelperService} from '../../../../services/record-helper.service';
import {LoadInnByInnBowlingRecordsAction} from '../../actions/records.actions';
import {IconProp} from '@fortawesome/fontawesome-svg-core';
import {IndividualBowlingDetailsDto} from '../../models/individual-bowling-details.dto';
import {NavigateValues} from '../../../../models/navigate.model';
import {faLink} from '@fortawesome/free-solid-svg-icons';

@Component({
    selector: 'app-bowling-by-innings-ui',
    templateUrl: './bowling-by-innings-ui.component.html',
    styleUrls: ['./bowling-by-innings-ui.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class BowlingByInningsUiComponent implements OnInit {
  @Input() bowlingInnByInn$!: Observable<InningsByInningsUiModel>;
  @Input() title!: string;
  @Input() searchType!: string

  @Output() dispatch: EventEmitter<{ payload: FindRecords }> = new EventEmitter();

  faLink = faLink;

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

  constructor(private router: Router,
              private route: ActivatedRoute,
              private bowlingStore: Store<BowlingOverallState>,
              private bowlingHelperService: BowlingHelperService,
              private recordHelperService: RecordHelperService) {
  }

  ngOnDestroy(): void {
    this.bowlInnByInnSub$.unsubscribe()
  }


  ngOnInit(): void {
    this.bowlingSummary$ = this.bowlingStore.select(s => {
      return s.playerRecordSummary;
    })

    this.route.queryParams.subscribe(params => {

      this.findBowlingParams = params as FindRecords
      let queryParams = this.bowlingHelperService.getHttpParamsFromFormDataForBowling(this.findBowlingParams).params.toString()

      this.venue = this.recordHelperService.setVenue(this.findBowlingParams.homeVenue.toLowerCase() == 'true',
        this.findBowlingParams.awayVenue.toLowerCase() == 'true',
        this.findBowlingParams.neutralVenue.toLowerCase() == 'true')

      this.dispatch.emit(LoadInnByInnBowlingRecordsAction({payload: this.findBowlingParams}))
      this.bowlingHelperService.loadSummaries(this.findBowlingParams, this.bowlingStore)

      let pageInfo = this.recordHelperService.getPageInformation(this.findBowlingParams)

      this.pageSize = pageInfo.pageSize
      this.pageNumber = pageInfo.pageNumber


      this.bowlInnByInnSub$ = this.bowlingInnByInn$.subscribe(payload => {
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

  getSortClass(sortOrder: SortOrder): IconProp {
    return this.recordHelperService.getSortClass(sortOrder, this.sortDirection)
  }

  getOvers(row: IndividualBowlingDetailsDto) {
    return this.bowlingHelperService.getOvers(row)
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

  getOversTitle() {
    if (this.isTheHundred())
      return 'Balls'
    else
      return 'Overs'
  }

  isTheHundred() {
    let subType = this.recordHelperService.getMatchSubType(this.bowlingSummary$)
    return subType.toLowerCase() == 'the hundred' || subType.toLowerCase() == "the women's hundred"
  }

  pageSizeChange($event: number) {
    this.pageSize = $event
  }

  getLabelForMaidens() {
    let matchSubType = this.recordHelperService.getMatchSubType(this.bowlingSummary$)
    if (matchSubType.toLowerCase() == 'the hundred' || matchSubType.toLowerCase() == "the women's hundred") return 'Dots'

    return 'Md'
  }

  getMaidensOrDots(maidens: number | null, dots: number | null) {
    let matchSubType = this.recordHelperService.getMatchSubType(this.bowlingSummary$)
    if (matchSubType.toLowerCase() == 'the hundred' || matchSubType.toLowerCase() == "the women's hundred")
      return dots == null ? '-' : dots
    return maidens == null ? '-' : maidens
  }

  getMaidensOrDotsSortOrder() {
    let matchSubType = this.recordHelperService.getMatchSubType(this.bowlingSummary$)
    if (matchSubType.toLowerCase() == 'the hundred' || matchSubType.toLowerCase() == "the women's hundred") return this.importedSortOrder.dots
    return this.importedSortOrder.maidens
  }

  getMatchLink(row: IndividualBowlingDetailsDto) {
    return "/scorecard/cardbyid/" + row.matchId
  }
}
