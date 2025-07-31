import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from '@angular/router';
import {ReactiveFormsModule} from '@angular/forms';
import {PlayerDetailsComponent} from './components/playerdetails/playerdetails.component';
import {StoreModule} from '@ngrx/store';
import {
  playerBattingOverallSuccessReducer,
  playerBiographySuccessReducer,
  playerBowlingOverallSuccessReducer,
  playerOverallSuccessReducer,
  playersSuccessReducer
} from './reducers/players.reducer';
import {EffectsModule} from '@ngrx/effects';
import {PlayerEffects} from './effects/players.effects';
import {BowlingDetailsComponent} from './components/playerdetails/bowling-details/bowling-details.component';
import {BattingDetailsComponent} from './components/playerdetails/batting-details/batting-details.component';
import {OverallDetailsComponent} from './components/playerdetails/overall-details/overall-details.component';
import {PlayerBiographyComponent} from './components/playerdetails/player-biography/player-biography.component';
import {GetPlayersComponent} from './components/getplayers/get-players.component';
import {PlayersListComponent} from './components/getplayers/players-list/players-list.component';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {SharedModule} from '../shared/shared.module';
import {
  MatchTypeBowlingDetailsComponent
} from "./components/playerdetails/matchtype-bowling-details/matchtype-bowling-details.component";
import {
  MatchTypeBattingDetailsComponent
} from "./components/playerdetails/matchtype-batting-details/matchtype-batting-details.component";


const routes: Routes = [
  {path: 'playerlist', component: GetPlayersComponent},
  {path: 'player/:id', component: PlayerDetailsComponent},
];

@NgModule({
  exports: [RouterModule, PlayerDetailsComponent, GetPlayersComponent],
  declarations: [GetPlayersComponent, PlayerDetailsComponent, BowlingDetailsComponent,
    MatchTypeBowlingDetailsComponent, MatchTypeBattingDetailsComponent, BattingDetailsComponent, OverallDetailsComponent, PlayerBiographyComponent, PlayersListComponent],
  imports: [CommonModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
    StoreModule.forFeature('players', {
      players: playersSuccessReducer
    }),
    StoreModule.forFeature('player', {
      playerBiography: playerBiographySuccessReducer,
      playerOverall: playerOverallSuccessReducer,
      playerBattingOverall: playerBattingOverallSuccessReducer,
      playerBowlingOverall: playerBowlingOverallSuccessReducer,
    }),
    EffectsModule.forFeature([PlayerEffects]),
    FontAwesomeModule,
    SharedModule
  ],
  providers: [
  ]
})
export class PlayerModule {
}
