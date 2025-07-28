import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs';

import {LoadPlayersAction} from '../../actions/players.actions';
import {PlayerState} from '../../models/app-state';
import {PlayerListUiModel} from '../../models/player';
import {FindPlayer} from '../../models/find-player.model';

@Component({
    selector: 'app-getplayers-list',
    templateUrl: './get-players.component.html',
    standalone: false
})
export class GetPlayersComponent implements OnInit {
  players$!: Observable<PlayerListUiModel>;
  name: string = ''
  team = ''
  exactMatch = false
  debutDate = ''
  activeUntilDate = ''
  private sortDirection!: string;
  private sortOrder!: number;

  constructor(private route: ActivatedRoute,
              private store: Store<PlayerState>) {

    this.players$ = this.store.select(s => s.players.players)
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {


      let fp = {...params} as FindPlayer
      this.name = fp.name
      this.team = fp.team ?? 'Any Team'
      this.exactMatch = fp.exactMatch
      this.debutDate = fp.startDate
      this.activeUntilDate = fp.endDate
      this.sortOrder = fp.sortOrder
      this.sortDirection = fp.sortDirection

      this.store.dispatch(LoadPlayersAction({payload: fp}))
    });

  }
}
