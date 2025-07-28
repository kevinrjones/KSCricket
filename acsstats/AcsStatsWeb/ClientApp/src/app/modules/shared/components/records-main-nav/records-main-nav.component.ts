import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable} from 'rxjs';
import {RecordsSummaryModel} from '../../../../models/records-summary.model';
import {NavigateValues} from '../../../../models/navigate.model'
import {FindRecords} from "../../../../models/find-records.model";

@Component({
    selector: 'app-records-header',
    templateUrl: './records-main-nav.component.html',
    styleUrls: ['./records-main-nav.component.css'],
    standalone: false
})
export class RecordsMainNavComponent implements OnInit {

  @Input() recordSummary$!: Observable<RecordsSummaryModel>;
  @Input() venue!: string;
  @Input() pageSize!: number
  @Input() totalItems!: number
  @Input() currentPage!: number

  @Input() returnUrl!: string;
  @Input() exportParams!: FindRecords;

  @Output() first: EventEmitter<void> = new EventEmitter();
  @Output() previous: EventEmitter<number> = new EventEmitter();
  @Output() next: EventEmitter<number> = new EventEmitter();
  @Output() last: EventEmitter<number> = new EventEmitter();
  @Output() goto: EventEmitter<NavigateValues> = new EventEmitter();
  @Output() pageSizeChange: EventEmitter<number> = new EventEmitter();
  @Output() exportData: EventEmitter<void> = new EventEmitter();


  constructor() {
  }

  ngOnInit(): void {
  }

  raiseFirst() {
    this.first.emit()
  }

  raiseGoto($event: NavigateValues) {
    this.goto.emit($event)
  }

  raisePrevious($event: number) {
    this.previous.emit($event)
  }

  raiseNext($event: number) {
    this.next.emit($event)
  }

  raiseLast($event: number) {
    this.last.emit($event)
  }

  onPageSizeChange($event: number) {
    this.pageSizeChange.emit($event)
  }
}
