import {ChangeDetectionStrategy, Component, Input, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {faCircleDown, faCircleUp} from '@fortawesome/free-solid-svg-icons';
import {
  BattlingLine,
  BowlingLine,
  Extras,
  Inning,
  Person,
  Scorecard, ScorecardHeader, ScorecardTeam
} from '../../../models/scorecard.model';
import {VictoryType} from "../../../../shared/models/victoryType";


@Component({
    selector: 'app-card-display',
    templateUrl: './card-display.component.html',
    styleUrls: ['./card-display.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class CardDisplayComponent implements OnInit {

  faCircleDown = faCircleDown
  faCircleUp = faCircleUp
  hideDetails: boolean[] = [false, false, false, false,];


  @Input() scorecard$!: Observable<Scorecard>;

  constructor() {
  }

  ngOnInit(): void {
    this.scorecard$.subscribe(
    )
  }

  isVictory(scorecard: Scorecard, teamKey: number) {
    return (scorecard.header.result.whoWon != null
      && scorecard.header.result.victoryType != VictoryType.Drawn
      && scorecard.header.result.victoryType != VictoryType.NoResult
      && scorecard.header.result.victoryType != VictoryType.Abandoned
      && scorecard.header.result.whoWon.key == teamKey)
  }

  getInningsDesc(inningsNumber: number) {
    return inningsNumber === 1 ? '1st' : '2nd'
  }

  getDismisal(battingLine: BattlingLine) {

    if (battingLine.dismissal.dismissalType == 0) {
      if (battingLine.dismissal.bowler.key == battingLine.dismissal.fielder.key) {
        return `c & b
        <a href="/player/${battingLine.dismissal.bowler.key}">${battingLine.dismissal.bowler.name}</a>`
      } else {
        return `c
          <a href="/player/${battingLine.dismissal.fielder.key}">${battingLine.dismissal.fielder.name}</a>
           b
          <a href="/player/${battingLine.dismissal.bowler.key}">${battingLine.dismissal.bowler.name}</a>`
      }
    } else if (battingLine.dismissal.dismissalType == 1) {
      return `b
        <a href="/player/${battingLine.dismissal.bowler.key}">${battingLine.dismissal.bowler.name}</a>`
    } else if (battingLine.dismissal.dismissalType == 2) {
      return `lbw b
        <a href="/player/${battingLine.dismissal.bowler.key}">${battingLine.dismissal.bowler.name}</a>`
    } else if (battingLine.dismissal.dismissalType == 3) {
      return `st
        <a href="/player/${battingLine.dismissal.fielder.key}">${battingLine.dismissal.fielder.name}</a>
         b
        <a href="/player/${battingLine.dismissal.bowler.key}">${battingLine.dismissal.bowler.name}</a>`
    } else if (battingLine.dismissal.dismissalType == 3) {
      return `Hit wicket b
        <a href="/player/${battingLine.dismissal.bowler.key}">${battingLine.dismissal.bowler.name}</a>`
    } else {
      return battingLine.dismissal.dismissal
    }
  }

  getFow(innings: Inning) {


    let fows: Array<string> = []

    innings.fallOfWickets.forEach(fow => {
      let score: string = fow.score != null ? fow.score.toString(10) : '?'
      let fowText = `${fow.wicket} - ${score} <a href="/player/${fow.player.key}">(${fow.player.name}</a>`
      if (fow.overs != null && fow.overs.length > 0) {
        fowText += `, ${fow.overs} ov`
      }
      fowText += ')'
      fows.push(fowText)
    })

    return fows.join(', ')
  }

  getTotal(innings: Inning) {
    if (innings.complete)
      return `${innings.total.total} all out`
    if (innings.declared)
      return `${innings.total.total}/${innings.total.wickets} dec`

    return `${innings.total.total}/${innings.total.wickets}`
  }

  getOverDetails(scorecard: Scorecard, innings: Inning) {
    let balls = innings.total.overs.split(".")
    let rr = ""
    let ballNumber = 1
    if (balls.length == 1) {
      ballNumber = parseInt(balls[0]) * scorecard.header.ballsPerOver
      rr = (innings.total.total * scorecard.header.ballsPerOver / ballNumber).toFixed(2)
    } else if (balls.length == 2) {
      ballNumber = (parseInt(balls[0]) * scorecard.header.ballsPerOver) + parseInt(balls[1])
      rr = (innings.total.total * scorecard.header.ballsPerOver / ballNumber).toFixed(2)
    }
    let minutesDetails = ""
    if (innings.total.minutes != -1 && innings.total.minutes != 0) minutesDetails = `, ${innings.total.minutes} Mts`
    let oversString = this.getOversString(scorecard)
    return `${innings.total.overs} ${oversString} (RR: ${rr} ${minutesDetails})`
  }

  getOversString(scorecard: Scorecard) {
    if(scorecard.header.matchTitle.includes("Hundred"))
      return "Balls"
    return "Overs"
  }

  getExtrasDetails(extras: Extras) {
    if (extras.total == 0) return ""
    let details = "("

    if (extras.byes != 0) details += `b ${extras.byes}, `
    if (extras.legByes != 0) details += `lb ${extras.legByes}, `
    if (extras.wides != 0) details += `w ${extras.wides}, `
    if (extras.noBalls != 0) details += `nb ${extras.noBalls}`

    if (details.endsWith(", ")) {
      details = details.slice(0, details.length - 2)
    }

    details += ")"
    return details
  }

  getStrikeRate(battingLine: BattlingLine) {
    if (battingLine.balls == null || battingLine.balls == 0) return "-"
    return (battingLine.runs * 100 / battingLine.balls).toFixed(2)
  }

  getEconomy(ballsPerOver: number, bowlingLine: BowlingLine) {
    let balls = bowlingLine.overs.split(".")
    let ballNumber = 0
    if (balls.length == 1) {
      ballNumber = parseInt(balls[0]) * ballsPerOver
    } else if (balls.length == 2) {
      ballNumber = (parseInt(balls[0]) * ballsPerOver) + parseInt(balls[1])
    }

    if (ballNumber == 0) return "-"
    return (bowlingLine.runs / ballNumber * ballsPerOver).toFixed(2)

  }

  getPeople = (officials: Person[]) => officials.map(u => u.name).join(", ");

  showPeople = (officials: Person[]) => officials.length > 0

  getPartnership(partnership: number, unbroken: boolean): string {
    if (unbroken) return `${partnership}*`
    else return `${partnership}`
  }

  hideOrShow(ndx: number) {
    if (ndx < this.hideDetails.length)
      this.hideDetails[ndx] = !this.hideDetails[ndx]
    else
      throw "Unexpected index value"
  }

  getShouldHide(ndx: number): boolean {
    if (ndx >= this.hideDetails.length)
      return false
    return !this.hideDetails[ndx]
  }


  getDetails(wicket: number, partnership: number, previousWicket: number, previousScore: number, multiple: boolean): string {
    if (multiple) return ""
    if (wicket === 1) {
      return `-/- to ${partnership}/${wicket}`
    } else {
      return `${previousScore}/${previousWicket} to ${previousScore + partnership}/${wicket}`
    }
  }

  getToss(scorecard: Scorecard) {
    if(scorecard != undefined
    && scorecard.header != undefined
    && scorecard.header.toss != undefined
    && scorecard.innings != undefined
    && scorecard.innings.length > 0
    && scorecard.innings[0].team != undefined) {
      let toss = scorecard.header.toss
      let team = scorecard.innings[0].team

      var batOrBowl = toss.key === team.key ? "bat" : "bowl"
      return `${toss.name} won the toss and decided to ${batOrBowl}`;
    }
    return "No toss made";
  }

  isNotAbandoned(header: ScorecardHeader) {
    return header.result.victoryType != VictoryType.Abandoned
  }
}
