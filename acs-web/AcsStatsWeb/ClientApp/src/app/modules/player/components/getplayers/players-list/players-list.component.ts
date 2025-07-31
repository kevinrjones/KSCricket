import {ChangeDetectionStrategy, Component, Input, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {PlayerListUiModel} from '../../../models/player';
import {DateTime} from 'luxon';
import {SortOrder} from '../../../../../models/sortorder.model';
import {IconProp} from '@fortawesome/fontawesome-svg-core';
import {RecordHelperService} from '../../../../../services/record-helper.service';
import {ActivatedRoute, Router} from '@angular/router';

interface SearchCriteria {
  exactMatch: boolean
  team: string
  startDate: string
  endDate: string
  sortOrder?: number
  sortDirection?: string

}

@Component({
    selector: 'app-players-list',
    templateUrl: './players-list.component.html',
    styleUrls: ['./players-list.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class PlayersListComponent implements OnInit {

  @Input() players$!: Observable<PlayerListUiModel>;
  @Input() name: string = ''
  @Input() team = ''
  @Input() debutDate = ''
  @Input() activeUntilDate = ''
  sortOrder!: number;
  importedSortOrder = SortOrder;
  private searchCriteria!: SearchCriteria;
  private sortDirection!: string;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private recordHelperService: RecordHelperService) {
  }

  private _exactMatch = false

  @Input() get exactMatch(): boolean {
    return this._exactMatch;
  }

  set exactMatch(value: boolean) {
    this._exactMatch = "" + value !== "false";
  }

  ngOnInit(): void {
    this.sortOrder = this.importedSortOrder.debutDate

    this.route.queryParams.subscribe(params => {

      this.searchCriteria = {...params} as SearchCriteria
    })

    this.players$.subscribe(payload => {
      this.sortOrder = payload.sortOrder
      this.sortDirection = payload.sortDirection
    })
  }

  getSortClass(sortOrder: SortOrder): IconProp {
    return this.recordHelperService.getSortClass(sortOrder, this.sortDirection)
  }

  getDate(date: string, replacementDateText: string): string {
    if (date == null || date == '') return replacementDateText

    const dt = DateTime.fromISO(date)
    return dt.toLocaleString(DateTime.DATE_FULL)
  }

  getNameAndTeam() {
    let text = `<span>'${this.name}'</span> playing for`
    if (this.team == "") text = text + " <span>any team</span>"
    else text = text + ` <span>${this.team}</span>`
    return text
  }

  getDisplayText() {
    return `You searched for ${this.getNameAndTeam()} with <span>${this.exactMatch ? "an exact match" : "no exact match"}</span>
    who is active between <span>${this.getDate(this.debutDate, 'any start date')}</span> and <span>${this.getDate(this.activeUntilDate, 'any end date')}`
  }

  sort(newSortOrder: SortOrder) {
    this.recordHelperService.sort(this.sortOrder, newSortOrder, this.sortDirection, this.router)
  }
}
