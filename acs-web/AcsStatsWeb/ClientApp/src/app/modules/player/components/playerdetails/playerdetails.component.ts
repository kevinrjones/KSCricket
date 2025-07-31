import {Component} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {PlayerBattingDetails, PlayerBiography, PlayerBowlingDetails, PlayerOverall} from '../../playerbiography.model';
import {Observable} from 'rxjs';
import {Store} from '@ngrx/store';
import {PlayerState} from '../../models/app-state';
import {
  LoadPlayerBattingDetailsAction,
  LoadPlayerBiographyAction,
  LoadPlayerBowlingDetailsAction,
  LoadPlayerOverallAction
} from '../../actions/players.actions';
import {PlayerHelpService} from "../../services/playerhelp.service";


@Component({
    selector: 'app-player-list',
    templateUrl: 'playerdetails.component.html',
    styleUrls: ['./playerdetails.component.css'],
    standalone: false
})
export class PlayerDetailsComponent {

  playerBiography$!: Observable<PlayerBiography>;
  playerOverall$!: Observable<ReadonlyArray<PlayerOverall[]>>;
  playerBattingOverall$!: Observable<{ [matchType: string]: PlayerBattingDetails[] }>;
  playerBowlingOverall$!: Observable<{ [matchType: string]: PlayerBowlingDetails[] }>;

  listOfSortedMatchType = ['wt', 'wo', 'witt', 'wf', 'wa', 'wtt', 't', 'o', 'itt', 'f', 'a', 'tt'];
  listOfSortedFCMatchType = ['wt', 'wf', 't', 'f'];
  listOfOtherMatchType = ['sec'];
  listOfSortedListAMatchType = ['wo', 'witt', 'wa', 'wtt', 'o', 'itt', 'a', 'tt'];
  previousMatchId: number = 0;


  constructor(private route: ActivatedRoute, private store: Store<PlayerState>, private playerHelperService: PlayerHelpService) {

    this.playerBiography$ = this.store.select(s => s.player.playerBiography)
    this.playerOverall$ = this.store.select(s => s.player.playerOverall)
    this.playerBattingOverall$ = this.store.select(s => s.player.playerBattingOverall)
    this.playerBowlingOverall$ = this.store.select(s => s.player.playerBowlingOverall)

  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      let selectedId = parseInt(params.get('id')!, 10);
      this.store.dispatch(LoadPlayerBiographyAction({payload: selectedId}));
      this.store.dispatch(LoadPlayerOverallAction({payload: selectedId}));
      this.store.dispatch(LoadPlayerBattingDetailsAction({payload: selectedId}));
      this.store.dispatch(LoadPlayerBowlingDetailsAction({payload: selectedId}));
    });
  }

  matchLabelFromMatchType(matchType: string) {
    return this.playerHelperService.getTitle(matchType);
  }
}
