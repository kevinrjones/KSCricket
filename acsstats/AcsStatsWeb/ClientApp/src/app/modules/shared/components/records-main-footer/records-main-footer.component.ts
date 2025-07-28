import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable} from 'rxjs';
import {RecordsSummaryModel} from '../../../../models/records-summary.model';
import {NavigateValues} from '../../../../models/navigate.model';

@Component({
    selector: 'app-records-footer',
    templateUrl: './records-main-footer.component.html',
    styleUrls: ['./records-main-footer.component.css'],
    standalone: false
})
export class RecordsMainFooterComponent implements OnInit {

  @Input()
  recordSummary$!: Observable<RecordsSummaryModel>;
  @Input()
  venue!: string;

  @Input() pageSize!: number
  @Input() totalItems!: number
  @Input() currentPage!: number

  @Output() first: EventEmitter<void> = new EventEmitter();
  @Output() previous: EventEmitter<number> = new EventEmitter();
  @Output() next: EventEmitter<number> = new EventEmitter();
  @Output() last: EventEmitter<number> = new EventEmitter();
  @Output() goto: EventEmitter<NavigateValues> = new EventEmitter();


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
}
