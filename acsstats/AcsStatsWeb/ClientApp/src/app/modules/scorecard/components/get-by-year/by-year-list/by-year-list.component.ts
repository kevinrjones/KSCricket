import {Component, Input} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs';
import {ScorecardState} from '../../../models/app-state';


@Component({
    selector: 'app-by-year-list',
    templateUrl: './by-year-list.component.html',
    styleUrls: ['./by-year-list.component.css'],
    standalone: false
})
export class ScorecardByYearListComponent {
  @Input() byYearList$!: Observable<string[]>

  constructor(private route: ActivatedRoute,
              private store: Store<ScorecardState>) {
  }

  ngOnInit() {
  }

  getEncodedValue(year: string) {
    return encodeURIComponent(year);
  }


}
