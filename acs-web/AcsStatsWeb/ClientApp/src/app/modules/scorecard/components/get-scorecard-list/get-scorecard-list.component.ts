import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {ScorecardListItem} from '../../models/scorecard-list-item.model';
import {ActivatedRoute} from '@angular/router';
import {Store} from '@ngrx/store';
import {ScorecardState} from '../../models/app-state';
import {FindScorecard} from '../../models/scorecard-search.model';
import {LoadScorecardListAction} from '../../actions/scorecard.actions';

@Component({
    selector: 'app-get-scorecard-list',
    templateUrl: './get-scorecard-list.component.html',
    styleUrls: ['./get-scorecard-list.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class GetScorecardListComponent implements OnInit {

  matchList$!: Observable<Array<ScorecardListItem>>;

  constructor(private route: ActivatedRoute,
              private store: Store<ScorecardState>) {

    this.matchList$ = this.store.select(s => s.scorecards.scorecards)

  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      let fs = params as FindScorecard
      this.store.dispatch(LoadScorecardListAction({payload: fs}))
    });

  }

}
