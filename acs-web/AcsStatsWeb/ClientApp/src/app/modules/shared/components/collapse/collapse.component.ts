import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {faCircleDown, faCircleUp} from '@fortawesome/free-solid-svg-icons';

@Component({
    selector: 'app-collapse',
    templateUrl: './collapse.component.html',
    styleUrls: ['./collapse.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class CollapseComponent implements OnInit {

  @Output() hideOrShow: EventEmitter<void> = new EventEmitter();
  @Input() hidden: boolean = false;

  faCircleDown = faCircleDown
  faCircleUp = faCircleUp


  constructor() {
  }

  ngOnInit(): void {
  }

  raiseHideOrShow() {
    this.hideOrShow.emit()
  }

}
