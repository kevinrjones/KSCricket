import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges
} from '@angular/core';
import {
    faAngleLeft,
    faAngleRight,
    faAnglesLeft,
    faAnglesRight,
    faCaretDown,
    faAngleDown,
    faUser
} from '@fortawesome/free-solid-svg-icons';
import {NavigateValues} from '../../../../models/navigate.model';
import {Router} from '@angular/router';
import {FindRecords} from '../../../../models/find-records.model';
import {FileService} from "../../services/fileService";
import {Location} from '@angular/common';
import {MenuItem} from "primeng/api";


@Component({
    selector: 'app-paging',
    templateUrl: './paging.component.html',
    styleUrls: ['./paging.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class PagingComponent implements OnInit, OnChanges {

  @Input() exportParams!: FindRecords
  @Input() pageSize!: number
  @Input() totalItems!: number
  @Input() currentPage!: number
  @Input() showGotoPage!: boolean;

  totalPages!: number
  currentFirstItem!: number;
  currentLastItem!: number;

  @Output() first: EventEmitter<void> = new EventEmitter();
  @Output() previous: EventEmitter<number> = new EventEmitter();
  @Output() next: EventEmitter<number> = new EventEmitter();
  @Output() last: EventEmitter<number> = new EventEmitter();
  @Output() goto: EventEmitter<NavigateValues> = new EventEmitter();
  @Output() pageSizeChange: EventEmitter<number> = new EventEmitter();
  @Output() exportData: EventEmitter<number> = new EventEmitter();
  gotoPageNumber: number;


  faAngleRight = faAngleRight;
  faAnglesRight = faAnglesRight;
  faAngleLeft = faAngleLeft;
  faAnglesLeft = faAnglesLeft;
  faCaretDown = faCaretDown;

  private currentUrl!: string;

  constructor(private router: Router, private fileService: FileService, private location: Location) {
    this.gotoPageNumber = 1
  }

  exportMenuItems: Array<MenuItem> = [
    {
      label: 'As Excel (this page)',
      command: () => this.downloadFile('xls')
    },
    {
      label: 'As Excel (all data)',
      command: () => this.downloadFile('xls', true)
    },
    {
      label: 'As CSV (this page)',
      command: () => this.downloadFile('csv')
    },
    {
      label: 'As CSV (all data)',
      command: () => this.downloadFile('csv', true)
    },
  ]

  ngOnChanges(changes: SimpleChanges): void {
    this.calculatePagingValues();
  }

  ngOnInit(): void {
    this.currentUrl = this.router.url;
    this.calculatePagingValues();
  }

  private static readonly FIRST_ITEM_INDEX = 1; // Extracted constant


  clickFirst() {
    if (this.currentFirstItem !== PagingComponent.FIRST_ITEM_INDEX) {
      this.first.emit()
    }
  }

  clickPrevious(): void {
    if (this.currentFirstItem !== 1) {
      const calculatedStartRow = this.calculatePreviousStartRow(this.currentFirstItem, this.pageSize);
      this.previous.emit(calculatedStartRow);
    }
  }

  private calculatePreviousStartRow(currentFirstItem: number, pageSize: number): number {
    const startRow = currentFirstItem - pageSize - 1;
    return Math.max(startRow, 0); // Ensures the startRow is non-negative
  }

  clickNext() {
    const isLastPage = this.currentPage === this.totalPages;
    if (!isLastPage) {
      const nextPageStartRow = this.getNextPageStartRow();
      this.next.emit(nextPageStartRow);
    }
  }

  private getNextPageStartRow(): number {
    return this.currentFirstItem + this.pageSize - 1;
  }

  clickLast(): void {
    if (this.currentPage !== this.totalPages) {
      const totalPages = this.calculateTotalPages();
      this.emitLastEvent(totalPages);
    }
  }

  private calculateTotalPages(): number {
    return Math.floor(this.totalItems / this.pageSize);
  }

  private emitLastEvent(totalPages: number): void {
    const startRow = totalPages * this.pageSize;
    this.last.emit(startRow);
  }

  clickGoto() {

    this.currentPage = this.gotoPageNumber;
    let startRow = (this.currentPage - 1) * this.pageSize

    while (startRow > this.totalItems) {
      this.currentPage--;
      startRow = (this.currentPage - 1) * this.pageSize
    }


    this.goto.emit({startRow: startRow, pageSize: this.pageSize})
  }

  shouldShowGotoControl() {
    return this.showGotoPage ? '' : 'hidePage'
  }

  isNavRightDisabled() {
    if (this.currentPage === this.totalPages || this.totalItems == 0) {
      return 'item-disabled'
    }
    return ''
  }

  isNavLeftDisabled() {
    if (this.currentFirstItem === 1 || this.totalItems == 0) {
      return 'item-disabled'
    }
    return ''
  }

  onPageSizeChange() {
    this.calculatePagingValues()
    this.gotoPageNumber = 1
    this.clickGoto()
    this.pageSizeChange.emit(this.pageSize)
  }

  raiseExportData() {
    this.exportData.emit()
  }

  downloadFile(fileType: string, fetchFullData: boolean = false): void {
    const BASE_URL_PATH = "/records/";
    const API_URL_PATH = "/api/";

    const buildDownloadUrl = (fileType: string): string => {
      const baseUrl = window.location.pathname.replace(BASE_URL_PATH, API_URL_PATH);
      const {matchType, teamId, opponentsId} = this.exportParams;
      return `${baseUrl}/${matchType}/${teamId}/${opponentsId}/${fileType}`;
    };

    const url = buildDownloadUrl(fileType);

    let params = {...this.exportParams};
    if (fetchFullData) {
      params.startRow = "0"
      params.pageSize = "10000"
      params.limit = "0"
    }

    this.fileService.downloadFile(url, params);
  }


  private calculatePagingValues(): void {
    const calculateCurrentFirstItem = (): number =>
      PagingComponent.FIRST_ITEM_INDEX + (this.pageSize * (this.currentPage - 1));

    const calculateCurrentLastItem = (): number => {
      const maxLastItem = this.pageSize + this.currentFirstItem - 1;
      return Math.min(this.totalItems, maxLastItem);
    };

    const calculateTotalPages = (): number => {
      const extraPage = this.totalItems % this.pageSize === 0 ? 0 : 1;
      return Math.floor(this.totalItems / this.pageSize) + extraPage;
    };

    this.currentFirstItem = calculateCurrentFirstItem();
    this.currentLastItem = calculateCurrentLastItem();
    this.totalPages = calculateTotalPages();
  }

    protected readonly faUser = faUser;
}
