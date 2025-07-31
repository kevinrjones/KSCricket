import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {Store} from '@ngrx/store';
import {LoadByDecadeAction} from '../../actions/scorecard.actions';
import {ScorecardState} from '../../models/app-state';

@Component({
    selector: 'app-get-by-decade',
    templateUrl: './get-by-decade.component.html',
    styleUrls: ['./get-by-decade.component.css'],
    standalone: false
})
export class GetByDecadeComponent implements OnInit {
  byYearList$!: Observable<{ [decade: number]: string[] }>;

  constructor(private route: ActivatedRoute,
              private store: Store<ScorecardState>) {

    this.byYearList$ = this.store.select(s => s.scorecards.decades)

  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.store.dispatch(LoadByDecadeAction({payload: params.get('name')!}))
    })
  }


}
