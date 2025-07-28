import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable, Subscription} from 'rxjs';
import {RecordsSummaryModel} from '../../../../models/records-summary.model';
import {InningsByInningsUiModel} from '../../models/team-overall-ui.model';
import {SortOrder} from '../../../../models/sortorder.model';
import {FindRecords} from '../../../../models/find-records.model';
import {ActivatedRoute, Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {TeamState} from '../../models/app-state';
import {TeamHelperService} from '../../services/team-helper.service';
import {RecordHelperService} from '../../../../services/record-helper.service';
import {IconProp} from '@fortawesome/fontawesome-svg-core';
import {NavigateValues} from '../../../../models/navigate.model';
import {IndividualTeamDetailsDto} from '../../models/individual-team-details.dto';
import {faLink} from '@fortawesome/free-solid-svg-icons';

@Component({
    selector: 'app-team-by-innings-ui',
    templateUrl: './team-innings-by-innings-ui.component.html',
    styleUrls: ['./team-innings-by-innings-ui.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class TeamInningsByInningsUiComponent implements OnInit {

  @Input() teamUIModel$!: Observable<InningsByInningsUiModel>;
  @Input() title!: string;
  @Input() searchType!: string
  @Input() showInnings!: boolean


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

      this.teamInnByInnSub$ = this.teamUIModel$.subscribe(payload => {
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

  getOversDisplay(ballsBowled: number, ballsPerOver: number) {
    if (ballsBowled == 0) return '-'

    if (this.recordHelperService.isTheHundred(this.teamSummary$))
      return `${ballsBowled}`

    let overs = ballsBowled / ballsPerOver
    let balls = ballsBowled % ballsPerOver

    return `${Math.floor(overs)}.${balls}`
  }

  pageSizeChange($event: number) {
    this.pageSize = $event
  }

  getScore(totalRuns: string, totalWickets: string, allOut: boolean, declared: boolean) {
    if (totalRuns == '0' && totalWickets == '0')
      return '-'

    let returnString = ""
    if (totalWickets == '10' || allOut)
      returnString = `${totalRuns}`
    else
      returnString = `${totalRuns}/${totalWickets}`

    if(declared)
      returnString = `${returnString}d`

    return returnString
  }

  getOversHeader() {
    return this.recordHelperService.isTheHundred(this.teamSummary$) ? 'Balls' : 'Overs'

  }

  getMatchLink(row: IndividualTeamDetailsDto) {
    return "/scorecard/cardbyid/" + row.caId
  }
}
