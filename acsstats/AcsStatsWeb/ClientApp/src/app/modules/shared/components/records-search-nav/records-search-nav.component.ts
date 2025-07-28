import {Component, Input, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../../../../models/app-state';
import {SaveRecordsFormAction} from '../../../../actions/form-state.actions';

@Component({
    selector: 'app-records-search-header',
    templateUrl: './records-search-nav.component.html',
    styleUrls: ['./records-search-nav.component.css'],
    standalone: false
})
export class RecordsSearchNavComponent implements OnInit {

  @Input() name!: string;

  battingClass: string
  bowlingClass: string
  fieldingClass: string
  teamClass: string
  partnershipClass: string
  umpiresClass: string

  constructor(private store: Store<AppState>) {
    this.battingClass = 'inactive'
    this.bowlingClass = 'inactive'
    this.fieldingClass = 'inactive'
    this.teamClass = 'inactive'
    this.partnershipClass = 'inactive'
    this.umpiresClass = 'inactive'

  }

  ngOnInit(): void {
    if (this.name == 'battingrecords') {
      this.battingClass = 'active'
    } else if (this.name == 'bowlingrecords') {
      this.bowlingClass = 'active'
    } else if (this.name == 'fieldingrecords') {
      this.fieldingClass = 'active'
    } else if (this.name == 'teamrecords') {
      this.teamClass = 'active'
    } else if (this.name == 'fowrecords') {
      this.partnershipClass = 'active'
    }
  }

  onClick() {
    this.store.dispatch(SaveRecordsFormAction({
      payload: {matchType: ''}
    }))
  }

}
