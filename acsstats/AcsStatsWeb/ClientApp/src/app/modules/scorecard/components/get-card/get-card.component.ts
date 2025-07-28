import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {Scorecard} from '../../models/scorecard.model';
import {ActivatedRoute} from '@angular/router';
import {Store} from '@ngrx/store';
import {ScorecardState} from '../../models/app-state';
import {LoadScorecardAction} from '../../actions/scorecard.actions';

@Component({
    selector: 'app-get-card',
    templateUrl: './get-card.component.html',
    styleUrls: ['./get-card.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class GetCardComponent implements OnInit {

  scorecard$!: Observable<Scorecard>;

  constructor(private route: ActivatedRoute,
              private store: Store<ScorecardState>) {
    this.scorecard$ = this.store.select(s => s.scorecards.scorecard)
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      let name = decodeURIComponent(params.get('name')!)
      this.store.dispatch(LoadScorecardAction({payload: name}))
    })
  }
}
