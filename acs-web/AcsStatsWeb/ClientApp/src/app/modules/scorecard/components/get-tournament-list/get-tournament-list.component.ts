import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {ScorecardListItem} from '../../models/scorecard-list-item.model';
import {ActivatedRoute} from '@angular/router';
import {Store} from '@ngrx/store';
import {ScorecardState} from '../../models/app-state';
import {LoadScorecardTournamentListAction} from '../../actions/scorecard.actions';

@Component({
    selector: 'app-get-tournament-list',
    templateUrl: './get-tournament-list.component.html',
    styleUrls: ['./get-tournament-list.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class GetTournamentListComponent implements OnInit {

  matchList$!: Observable<Array<ScorecardListItem>>;

  constructor(private route: ActivatedRoute,
              private store: Store<ScorecardState>) {

    this.matchList$ = this.store.select(s => s.scorecards.scorecardTournaments)
  }

  // todo: 1
  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      let name = decodeURIComponent(params.get('tournament')!)
      this.store.dispatch(LoadScorecardTournamentListAction({payload: name}))
    })
  }


}
