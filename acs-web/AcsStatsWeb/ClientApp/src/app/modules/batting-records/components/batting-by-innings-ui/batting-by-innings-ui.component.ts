import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable, Subscription} from 'rxjs';
import {RecordsSummaryModel} from '../../../../models/records-summary.model';
import {InningsByInningsUiModel} from '../../models/batting-overall-ui.model';
import {SortOrder} from '../../../../models/sortorder.model';
import {FindRecords} from '../../../../models/find-records.model';
import {ActivatedRoute, Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {BattingOverallState} from '../../models/app-state';
import {BattingHelperService} from '../../services/batting-helper.service';
import {RecordHelperService} from '../../../../services/record-helper.service';
import {IndividualBattingDetailsDto} from '../../models/individual-batting-details.dto';
import {IconProp} from '@fortawesome/fontawesome-svg-core';
import {NavigateValues} from '../../../../models/navigate.model';
import {faLink} from '@fortawesome/free-solid-svg-icons';
import {DateTime} from "luxon";
import {DateHelperService} from "../../../shared/services/dateHelperService";

@Component({
  selector: 'app-batting-by-innings-ui',
  templateUrl: './batting-by-innings-ui.component.html',
  styleUrls: ['./batting-by-innings-ui.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class BattingByInningsUiComponent implements OnInit {

  @Input() battingUIModel$!: Observable<InningsByInningsUiModel>;
  @Input() title!: string;
  @Input() isSingleInnings: boolean = true;
  @Input() searchType!: string

  @Output() dispatch: EventEmitter<{ payload: FindRecords }> = new EventEmitter();

  battingSummary$!: Observable<RecordsSummaryModel>;
  sortOrder!: number;
  importedSortOrder = SortOrder;
  pageSize!: number;
  pageNumber!: number;
  venue!: string;
  findBattingParams!: FindRecords;
  count!: number;
  currentPage!: number;
  faLink = faLink;
  private sortDirection!: string;
  private batInnByInnSub$!: Subscription;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private battingStore: Store<BattingOverallState>,
              private battingHelperService: BattingHelperService,
              private recordHelperService: RecordHelperService,
              public dateHelperService: DateHelperService
  ) {

  }

  ngOnDestroy(): void {
    this.batInnByInnSub$.unsubscribe()
  }

  ngOnInit(): void {

    this.battingSummary$ = this.battingStore.select(s => {
      return s.playerRecordSummary;
    })

    this.route.queryParams.subscribe(params => {

      this.findBattingParams = params as FindRecords

      this.venue = this.recordHelperService.setVenue(this.findBattingParams.homeVenue.toLowerCase() == 'true',
        this.findBattingParams.awayVenue.toLowerCase() == 'true',
        this.findBattingParams.neutralVenue.toLowerCase() == 'true')

      this.dispatch.emit({payload: this.findBattingParams})

      this.battingHelperService.loadSummaries(this.findBattingParams, this.battingStore)

      let pageInfo = this.recordHelperService.getPageInformation(this.findBattingParams)

      this.pageSize = pageInfo.pageSize
      this.pageNumber = pageInfo.pageNumber

      this.batInnByInnSub$ = this.battingUIModel$.subscribe(payload => {
        this.sortOrder = payload.sortOrder
        this.sortDirection = payload.sortDirection
        this.count = payload.sqlResults.count;
        this.currentPage = this.recordHelperService.getCurrentPage(this.findBattingParams)
      })

    });

  }

  sort(newSortOrder: SortOrder
  ) {
    this.recordHelperService.sort(this.sortOrder, newSortOrder, this.sortDirection, this.router)
  }

  formatHighestScore(row: IndividualBattingDetailsDto
  ) {
    let notOut = true
    if(!this.isSingleInnings) {
      if (row.notOut1 !== null && row.notOut1 !== undefined && row.bat1 !== null && row.bat1 !== undefined) {
        notOut = row.notOut1;
      }
      if (row.notOut2 !== null && row.notOut2 !== undefined && row.bat2 !== null && row.bat2 !== undefined) {
        notOut = notOut && row.notOut2;
      }
    } else {
      notOut = row.notOut;
    }
    return this.battingHelperService.formatHighestScore(notOut, row.playerScore, row.bat1, row.bat2, row.notOut1, row.notOut2, this.isSingleInnings)
  }

  getSortClass(sortOrder: SortOrder
  ): IconProp {
    return this.recordHelperService.getSortClass(sortOrder, this.sortDirection)
  }

  gotoPage(navigateValues: NavigateValues
  ) {
    this.recordHelperService.navigateToPage(navigateValues.startRow, navigateValues.pageSize, this.router)
  }

  navigate(startRow: number
  ) {
    this.recordHelperService.navigateToPage(startRow, this.pageSize, this.router)
  }

  getIndex(ndx: number
  ) {
    return ((this.currentPage - 1) * this.pageSize) + ndx + 1
  }

  pageSizeChange($event: number
  ) {
    this.pageSize = $event
  }

  getMatchLink(row: IndividualBattingDetailsDto
  ) {
    return "/scorecard/cardbyid/" + row.matchId
  }

}
