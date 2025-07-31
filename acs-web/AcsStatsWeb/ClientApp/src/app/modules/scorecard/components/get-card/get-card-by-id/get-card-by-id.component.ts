import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {Scorecard} from '../../../models/scorecard.model';
import {ActivatedRoute} from '@angular/router';
import {Store} from '@ngrx/store';
import {ScorecardState} from '../../../models/app-state';
import {LoadScorecardByIdAction} from '../../../actions/scorecard.actions';

@Component({
    selector: 'app-get-card-by-id',
    templateUrl: './get-card-by-id.component.html',
    styleUrls: ['./get-card-by-id.component.css'],
    standalone: false
})
export class GetCardByIdComponent implements OnInit {

  scorecard$!: Observable<Scorecard>;

  constructor(private route: ActivatedRoute,
              private store: Store<ScorecardState>) {
    this.scorecard$ = this.store.select(s => s.scorecards.scorecard)
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      let id = params.get('id')!
      this.store.dispatch(LoadScorecardByIdAction({payload: id}))
    })
  }
}
