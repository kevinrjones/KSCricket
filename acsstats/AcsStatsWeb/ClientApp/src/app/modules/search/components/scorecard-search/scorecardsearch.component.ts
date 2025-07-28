import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Store} from "@ngrx/store";
import {FindScorecardState} from "../../models/find-scorecard.models";
import {take} from "rxjs/operators";
import {SaveScorecardSearchFormAction} from "../../actions/search-state.actions";
import {SearchState} from "../../models/app-state";


@Component({
    selector: 'app-scorecard-search',
    templateUrl: './scorecardsearch.component.html',
    styleUrls: ['./scorecardsearch.component.css'],
    standalone: false
})
export class ScorecardSearchComponent {
  type: string = '';

  scorecardSearchForm!: FormGroup;
  homeTeamMessage: String = '';
  awayTeamMessage: String = '';

  private validationMessages = {
    required: 'Please enter a name',
    minlength: 'The name must be at least 3 characters'
  }
  private scorecardState!: FindScorecardState | undefined;

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private store: Store<SearchState>) {
  }

  ngOnInit() {

    this.store.select(s => s)
      .pipe(take(1))
      .subscribe((f: SearchState) => this.scorecardState = f.search.scorecardSearch);

    let teamName = (this.scorecardState?.homeTeam != undefined) ? this.scorecardState.homeTeam : ''
    let opponentsName = (this.scorecardState?.awayTeam != undefined) ? this.scorecardState.awayTeam : ''
    let exactTeamMatch = (this.scorecardState?.homeTeamExactMatch != undefined) ? this.scorecardState.homeTeamExactMatch : false
    let exactOpponentsMatch = (this.scorecardState?.aweayTeamExactMatch != undefined) ? this.scorecardState.aweayTeamExactMatch : false
    let startDate = (this.scorecardState?.startDate != undefined) ? this.scorecardState.startDate : ''
    let endDate = (this.scorecardState?.endDate != undefined) ? this.scorecardState.endDate : ''
    let venue = (this.scorecardState?.venue != undefined) ? this.scorecardState.venue : 0
    let matchType = (this.scorecardState?.matchType != undefined) ? this.scorecardState.matchType : 'all'
    let matchResult = (this.scorecardState?.matchResult != undefined) ? this.scorecardState.matchResult : '0'


    this.scorecardSearchForm = this.formBuilder.group({
      homeTeam: [teamName, [Validators.required, Validators.minLength(3)]],
      homeTeamExactMatch: exactTeamMatch,
      awayTeam: [opponentsName, [Validators.required, Validators.minLength(3)]],
      awayTeamExactMatch: exactOpponentsMatch,
      venue: venue,
      startDate: startDate,
      endDate: endDate,
      matchType: matchType,
      matchResult: matchResult,
    });

    const homeTeam = this.scorecardSearchForm.get('homeTeam')

    homeTeam?.valueChanges.subscribe(
      _ => this.setHomeTeamMessage(homeTeam)
    )

    const awayTeam = this.scorecardSearchForm.get('awayTeam')

    awayTeam?.valueChanges.subscribe(
      _ => this.setAwayTeamMessage(awayTeam)
    )

  }

  find() {

    this.router.navigate(['/scorecardlist'], {
      queryParams: {
        homeTeam: this.scorecardSearchForm.get('homeTeam')?.value
        , homeTeamExactMatch: this.scorecardSearchForm.get('homeTeamExactMatch')?.value
        , awayTeam: this.scorecardSearchForm.get('awayTeam')?.value
        , awayTeamExactMatch: this.scorecardSearchForm.get('awayTeamExactMatch')?.value
        , venue: this.scorecardSearchForm.get('venue')?.value
        , startDate: this.scorecardSearchForm.get('startDate')?.value
        , endDate: this.scorecardSearchForm.get('endDate')?.value
        , matchType: this.scorecardSearchForm.get('matchType')?.value
        , matchResult: this.scorecardSearchForm.get('matchResult')?.value
      }
    })

    this.store.dispatch(SaveScorecardSearchFormAction({
      payload:
        this.scorecardSearchForm.getRawValue()
    }))

  }


  private setHomeTeamMessage(nameControl: AbstractControl): void {
    this.homeTeamMessage = ''

    if ((nameControl.touched || nameControl.dirty) && nameControl.errors) {
      this.homeTeamMessage = Object.keys(nameControl.errors).map(
        key => (this.validationMessages as any)[key]).join(' ');
    }
  }

  private setAwayTeamMessage(nameControl: AbstractControl): void {
    this.awayTeamMessage = ''

    if ((nameControl.touched || nameControl.dirty) && nameControl.errors) {
      this.awayTeamMessage = Object.keys(nameControl.errors).map(
        key => (this.validationMessages as any)[key]).join(' ');
    }
  }
}
