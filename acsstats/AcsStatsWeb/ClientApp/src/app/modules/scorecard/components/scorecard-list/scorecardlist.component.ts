import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Store} from '@ngrx/store';
import {ScorecardState} from '../../models/app-state';
import {Observable} from 'rxjs';
import {ScorecardListItem} from '../../models/scorecard-list-item.model';

@Component({
    selector: 'app-scorecard-list',
    templateUrl: './scorecardlist.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class ScorecardListComponent {
  @Input() matchList$!: Observable<Array<ScorecardListItem>>;

  constructor(private route: ActivatedRoute,
              private store: Store<ScorecardState>) {

    this.matchList$ = this.store.select(s => s.scorecards.scorecards)

  }

  // todo: 2
  ngOnInit() {
    // this.route.queryParams.subscribe(params => {
    //   let fs = params as FindScorecard
    //   this.store.dispatch(LoadScorecardListAction({payload: fs}))
    // });

  }

  getEncodedValue(key: string) {
    return encodeURIComponent(key);
  }

}
