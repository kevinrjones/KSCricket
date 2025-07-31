export class PlayerDetail {
  constructor(public fullName: string,
              public activeUntil: number,
              public birthDate: string,
              public dateDied: string,
              public battingHand: string,
              public bowlingMode: string) {
  }
}

export class PlayerBiography {
  constructor(public playerDetails: PlayerDetail) {
  }
}

export class PlayerOverall {
  constructor(
    public team: string, public matchType: string, public teamId: number, public matches: number,
    public runs: number, public innings: number, public notouts: number, public balls: number,
    public fours: number, public sixes: number, public hundreds: number, public fifties: number,
    public highestScore: number,
    public bowlingBalls: number, public bowlingRuns: number, public maidens: number, public wickets: number,
    public bowlingFours: number, public bowlingSixes: number, public wides: number, public noBalls: number
  ) {
  }
}

export class PlayerBattingDetails {
  constructor(public matchId: string, public matchType: string, public matchTitle: string, public team: string, public opponents: string, public ground: string, public matchStartDate: string,
              public inningsNumber: number, public dismissal: string, public dismissalType: number, public fielderId: number, public fielderName: number, public bowlerId: number,
              public bowlerName: string, public score: number, public position: number, public notOut: number, public balls: number, public minutes: number, public fours: number, public sixes: number,
              public captain: number, public wicketKeeper: number, public sr: number) {
  }
}

export class PlayerBowlingDetails {
  constructor(public matchId: string, public matchType: string, public matchTitle: string, public team: string, public opponents: string, public ground: string,
              public matchStartDate: string, public inningsNumber: number, public balls: number, public runs: number,
              public maidens: number, public wickets: number, public dots: number, public fours: number, public sixes: number,
              public wides: number, public noBalls: number, public captain: number, public ballsPerOver: number) {
  }
}

