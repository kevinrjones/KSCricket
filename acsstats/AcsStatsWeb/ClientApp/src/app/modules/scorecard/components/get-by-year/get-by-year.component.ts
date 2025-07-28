import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {Store} from '@ngrx/store';
import {ScorecardState} from '../../models/app-state';
import {LoadByYearAction} from '../../actions/scorecard.actions';

@Component({
    selector: 'app-get-by-year',
    templateUrl: './get-by-year.component.html',
    styleUrls: ['./get-by-year.component.css'],
    standalone: false
})
export class GetByYearComponent implements OnInit {

  byYearList$!: Observable<string[]>

  constructor(private route: ActivatedRoute,
              private store: Store<ScorecardState>) {

    this.byYearList$ = this.store.select(s => s.scorecards.tournamentsBySeason)

  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.store.dispatch(LoadByYearAction({payload: {type: params.get('name')!, year: params.get('year')!}}))
    });

  }

}
