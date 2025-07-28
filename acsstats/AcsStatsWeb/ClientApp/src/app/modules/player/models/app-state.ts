import {PlayerListUiModel} from './player';
import {PlayerBattingDetails, PlayerBiography, PlayerBowlingDetails, PlayerOverall} from '../playerbiography.model';


export interface PlayerState {
  players: { players: PlayerListUiModel },
  player: {
    playerBiography: PlayerBiography,
    playerOverall: ReadonlyArray<PlayerOverall[]>,
    playerBattingOverall: { [matchType: string]: PlayerBattingDetails[] },
    playerBowlingOverall: { [matchType: string]: PlayerBowlingDetails[] }
  },
}

