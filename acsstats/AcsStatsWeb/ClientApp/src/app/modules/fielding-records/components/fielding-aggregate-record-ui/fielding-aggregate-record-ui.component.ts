import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable, Subscription} from 'rxjs';
import {FieldingOverallUiModel} from '../../models/fielding-overall-ui.model';
import {RecordsSummaryModel} from '../../../../models/records-summary.model';
import {SortOrder} from '../../../../models/sortorder.model';
import {FindRecords} from '../../../../models/find-records.model';
import {ActivatedRoute, Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {FieldingOverallState} from '../../models/app-state';
import {FieldingHelperService} from '../../services/fielding-helper.service';
import {RecordHelperService} from '../../../../services/record-helper.service';
import {FieldingCareerRecordDto} from '../../models/fielding-overall.model';
import {IconProp} from '@fortawesome/fontawesome-svg-core';
import {NavigateValues} from '../../../../models/navigate.model';

@Component({
    selector: 'app-fielding-aggregate-record-ui',
    templateUrl: './fielding-aggregate-record-ui.component.html',
    styleUrls: ['./fielding-aggregate-record-ui.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class FieldingAggregateRecordUiComponent implements OnInit {

  @Input() fieldingUIModel$!: Observable<FieldingOverallUiModel>;
  @Input() title!: string;
  @Input() showTeam!: boolean
  @Input() showOpposition!: boolean
  @Input() showCountry!: boolean
  @Input() showGround!: boolean
  @Input() showYear!: boolean
  @Input() showSeason!: boolean
  @Input() searchType!: string

  @Output() dispatch: EventEmitter<{ payload: FindRecords }> = new EventEmitter();

  fieldingSummaryModel$!: Observable<RecordsSummaryModel>;
  sortOrder!: number;
  importedSortOrder = SortOrder;
  venue!: string;
  pageSize!: number;
  pageNumber!: number;
  findFieldingParams!: FindRecords;
  count!: number;
  currentPage!: number;
  private sortDirection!: string;
  private fieldingUIModelSub$!: Subscription;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private fieldingStore: Store<FieldingOverallState>,
              private fieldingHelperService: FieldingHelperService,
              private recordHelperService: RecordHelperService) {
  }

  ngOnDestroy() {
    this.fieldingUIModelSub$.unsubscribe();
  }

  ngOnInit(): void {
    this.showTeam = true

    this.fieldingSummaryModel$ = this.fieldingStore.select(s => {
      return s.playerRecordSummary;
    })

    this.route.queryParams.subscribe(params => {

      this.findFieldingParams = params as FindRecords

      this.venue = this.recordHelperService.setVenue(this.findFieldingParams.homeVenue.toLowerCase() == 'true',
        this.findFieldingParams.awayVenue.toLowerCase() == 'true',
        this.findFieldingParams.neutralVenue.toLowerCase() == 'true')

      this.dispatch.emit({payload: this.findFieldingParams})
      this.fieldingHelperService.loadSummaries(this.findFieldingParams, this.fieldingStore)

      let pageInfo = this.recordHelperService.getPageInformation(this.findFieldingParams)

      this.pageSize = pageInfo.pageSize
      this.pageNumber = pageInfo.pageNumber

      this.fieldingUIModelSub$ = this.fieldingUIModel$.subscribe(payload => {
        this.sortOrder = payload.sortOrder
        this.sortDirection = payload.sortDirection

        this.count = payload.sqlResults.count;

        this.currentPage = this.recordHelperService.getCurrentPage(this.findFieldingParams)
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

  pageSizeChange($event: number) {
    this.pageSize = $event
  }

  formatBest(row: FieldingCareerRecordDto) {
    if (row.bestCaughtKeeper + row.bestStumpings == row.bestDismissals)
      return `${row.bestDismissals} (${row.bestCaughtKeeper}ct, ${row.bestStumpings}st)`
    return `${row.bestCaughtFielder} (${row.bestCaughtFielder}ct, 0st)`
  }
}
