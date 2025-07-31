import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable, Subscription} from 'rxjs';
import {RecordsSummaryModel} from '../../../../models/records-summary.model';
import {InningsByInningsUiModel} from '../../models/fow-overall-ui.model';
import {SortOrder} from '../../../../models/sortorder.model';
import {FindRecords} from '../../../../models/find-records.model';
import {ActivatedRoute, Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {FowOverallState} from '../../models/app-state';
import {FowHelperService} from '../../services/fow-helper.service';
import {RecordHelperService} from '../../../../services/record-helper.service';
import {IndividualFowDetailsDto} from '../../models/individual-fow-details.dto';
import {IconProp} from '@fortawesome/fontawesome-svg-core';
import {NavigateValues} from '../../../../models/navigate.model';
import {faLink} from '@fortawesome/free-solid-svg-icons';
import {DateTime} from 'luxon';

@Component({
    selector: 'app-fow-by-innings-ui',
    templateUrl: './fow-by-innings-ui.component.html',
    styleUrls: ['./fow-by-innings-ui.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class FowByInningsUiComponent implements OnInit {

  @Input() fowUIModel$!: Observable<InningsByInningsUiModel>;
  @Input() title!: string;
  @Input() showWicketDetails = true;
  @Input() searchType!: string
  @Output() dispatch: EventEmitter<{ payload: FindRecords }> = new EventEmitter();

  faLink = faLink
  fowSummary$!: Observable<RecordsSummaryModel>;
  sortOrder!: number;
  importedSortOrder = SortOrder;
  pageSize!: number;
  pageNumber!: number;
  venue!: string;
  findFowParams!: FindRecords;
  count!: number;
  currentPage!: number;
  private sortDirection!: string;
  private batInnByInnSub$!: Subscription;
  private queryParams: string | null = null;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private fowStore: Store<FowOverallState>,
              private fowHelperService: FowHelperService,
              private recordHelperService: RecordHelperService) {
  }

  ngOnDestroy(): void {
    this.batInnByInnSub$.unsubscribe()
  }

  ngOnInit(): void {
    this.fowSummary$ = this.fowStore.select(s => {
      return s.playerRecordSummary;
    })

    this.route.queryParams.subscribe(params => {

      this.findFowParams = params as FindRecords
      let partnershipWicket = parseInt(this.findFowParams.partnershipWicket) ;
      this.queryParams = this.fowHelperService.getHttpParamsFromFormData(this.findFowParams, partnershipWicket).params.toString()

      this.venue = this.recordHelperService.setVenue(this.findFowParams.homeVenue.toLowerCase() == 'true',
        this.findFowParams.awayVenue.toLowerCase() == 'true',
        this.findFowParams.neutralVenue.toLowerCase() == 'true')

      this.dispatch.emit({payload: this.findFowParams})

      this.fowHelperService.loadSummaries(this.findFowParams, this.fowStore)

      let pageInfo = this.recordHelperService.getPageInformation(this.findFowParams)

      this.pageSize = pageInfo.pageSize
      this.pageNumber = pageInfo.pageNumber

      this.batInnByInnSub$ = this.fowUIModel$.subscribe(payload => {
        this.sortOrder = payload.sortOrder
        this.sortDirection = payload.sortDirection
        this.count = payload.sqlResults.count;
        this.currentPage = this.recordHelperService.getCurrentPage(this.findFowParams)
      })

    });

  }

  sort(newSortOrder: SortOrder) {
    this.recordHelperService.sort(this.sortOrder, newSortOrder, this.sortDirection, this.router)
  }

  formatHighestScore(row: IndividualFowDetailsDto) {
    return this.fowHelperService.formatHighestScore(row.unbroken1, row.runs)
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

  pageSizeChange($event: number) {
    this.pageSize = $event
  }

  getRuns(runs: number, unbroken1: boolean) {
    if (unbroken1)
      return `${runs}*`
    return `${runs}`
  }

  getInScore(previousScore: number | null, previousWicket: number, currentScore: number, wicket: number, partnership: number) {
    if (previousScore == null || previousScore == -1)
      return `${currentScore - partnership}/${wicket - 1}`

    return `${previousScore}/${previousWicket}`
  }

  getOutScore(currentScore: number, wicket: number, unbroken: boolean) {
    if (unbroken)
      return `${currentScore}/${wicket - 1}`

    return `${currentScore}/${wicket}`
  }

  getMatchLink(row: IndividualFowDetailsDto) {
    return "/scorecard/cardbyid/" + row.matchId
  }

  toFormattedDate(date: string) {
    return DateTime.fromFormat(date, "yyyy-MM-dd").toLocaleString(DateTime.DATE_MED)
  }

}
