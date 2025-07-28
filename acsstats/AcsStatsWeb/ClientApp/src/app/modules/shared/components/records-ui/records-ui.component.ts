import {Component, Input, OnInit, TemplateRef} from '@angular/core';
import {SaveRecordsFormAction} from '../../../../actions/form-state.actions';
import {LoadMatchSubTypesAction} from '../../../../actions/match-sub-types.actions';
import {LoadTeamsAction} from '../../../../actions/teams.actions';
import {LoadCountriesAction} from '../../../../actions/countries.actions';
import {LoadGroundsAction} from '../../../../actions/grounds.actions';
import {LoadMatchDatesAction, LoadSeriesDatesAction,} from '../../../../actions/dates.actions';
import {Store} from '@ngrx/store';
import {AppState} from '../../../../models/app-state';
import {FormHelperService} from '../../../../services/form-helper.service';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Observable, startWith, Subscription, switchMap} from 'rxjs';
import {Team} from '../../../../models/team.model';
import {Country} from '../../../../models/country.model';
import {defaultGround, Ground} from '../../../../models/ground.model';
import {MatchDate} from '../../../../models/date.model';
import {MatchSubTypeModel} from '../../../../models/match-sub-type.model';
import {DateTime} from 'luxon';
import {Router} from '@angular/router';
import {map, take} from 'rxjs/operators';
import {FindFormState} from '../../../../models/find-form.state';
import {AutoCompleteCompleteEvent, AutoCompleteSelectEvent} from "primeng/autocomplete";
import {formatNumber} from "@angular/common";

@Component({
  selector: 'app-records-ui',
  templateUrl: './records-ui.component.html',
  styleUrls: ['./records-ui.component.css'],
  standalone: false
})
export class RecordsUIComponent implements OnInit {
  recordsForm!: FormGroup;
  teams$!: Observable<Array<Team>>;
  countries$!: Observable<Array<Country>>;
  grounds$!: Observable<Array<Ground>>;
  filteredGrounds$!: Observable<Array<Ground>>;
  seriesDates$!: Observable<Array<string>>;
  matchDates$!: Observable<Array<MatchDate>>;
  matchSubTypes$!: Observable<MatchSubTypeModel[]>;
  @Input()
  defaultSortOrder: number = 4
  @Input()
  defaultSortDirection = 'DESC'
  @Input()
  urlRoot!: string
  @Input()
  limitText!: string
  @Input()
  initialLimit!: number
  @Input()
  selectList!: TemplateRef<any>;
  private readonly defaultMatchType: string;
  private matchTypeControlSub?: Subscription;
  private matchSubTypeControlSub?: Subscription;
  private countriesControlSub?: Subscription;
  private matchDateSub?: Subscription;
  private seriesDateSub?: Subscription;
  private formState$: Observable<FindFormState>;
  private findRecords!: FindFormState | undefined;

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private store: Store<AppState>,
              private formHelperService: FormHelperService) {

    this.defaultMatchType = formHelperService.getDefaultMatchType()

    this.teams$ = this.store.select(s => s.teams)
    this.countries$ = this.store.select(s => s.countries)
    this.grounds$ = this.store.select(s => s.grounds)
    this.seriesDates$ = this.store.select(s => s.seriesDates)
    this.matchDates$ = this.store.select(s => s.matchDates)
    this.matchSubTypes$ = this.store.select(s => s.matchSubTypes)
    this.formState$ = this.store.select(s => s.formState)
  }

  get limit() {
    return this.recordsForm.get('limit');
  }

  ngOnInit(): void {
    const {teamId, opponentsId} = this.initializeFormFromStore();

    let matchType = this.getMatchTypeForForm();

    // if the subtype is not found set it to the matchtype
    let matchSubType = (this.findRecords?.matchSubType != undefined && this.findRecords?.matchSubType !== '') ?
      this.findRecords?.matchSubType
      : matchType;

    let countryId = (this.findRecords?.hostCountryId != undefined) ?
      this.findRecords?.hostCountryId
      : 0;

    let startDate = (this.findRecords?.startDate != undefined) ? this.findRecords.startDate : ''
    let endDate = (this.findRecords?.endDate != undefined) ? this.findRecords.endDate : ''
    let season = (this.findRecords?.season != undefined) ? this.findRecords.season : ''
    let isTeamBattingRecord = (this.findRecords?.isTeamBattingRecord != undefined) ? this.findRecords.isTeamBattingRecord : true
    let isForWicket = (this.findRecords?.isForWicket != undefined) ? this.findRecords.isForWicket : false

    this.recordsForm = this.formBuilder.group({
      matchType: matchType,
      matchSubType: matchType,
      pageSize: 50,
      limit: new FormControl(this.initialLimit, Validators.required),
      fivesLimit: 5,
      teamId: teamId,
      opponentsId: opponentsId,
      homeVenue: false,
      awayVenue: false,
      neutralVenue: false,
      hostCountryId: 0,
      groundId: defaultGround(),
      startDate: startDate,
      endDate: endDate,
      season: 0,
      matchWon: 0,
      matchLost: 0,
      matchDrawn: 0,
      matchTied: 0,
      format: 1,
      isTeamBattingRecord,
      sortOrder: this.defaultSortOrder,
      sortDirection: this.defaultSortDirection,
      isForWicket
    });

    if (this.findRecords != undefined && this.findRecords.matchType != '') {
      this.recordsForm.patchValue({
        matchType: this.findRecords?.matchType,
        matchSubType: this.findRecords?.matchSubType,
        teamId: this.findRecords?.teamId,
        opponentsId: this.findRecords?.opponentsId,
        limit: this.findRecords?.limit,
        startDate: this.findRecords?.startDate,
        endDate: this.findRecords?.endDate,
        season: this.findRecords?.season,
        awayVenue: this.findRecords?.awayVenue,
        homeVenue: this.findRecords?.homeVenue,
        neutralVenue: this.findRecords?.neutralVenue,
        hostCountryId: this.findRecords?.hostCountryId,
        groundId: this.findRecords?.groundId,
        matchWon: this.findRecords?.matchWon,
        matchLost: this.findRecords?.matchLost,
        matchDrawn: this.findRecords?.matchDrawn,
        matchTied: this.findRecords?.matchTied,
        format: this.findRecords?.format,
        isTeamBattingRecord: this.findRecords?.isTeamBattingRecord,
        isForWicket: this.findRecords?.isForWicket
      })
    } else {
      this.dispatchInitializationActions(matchType);
      this.dispatchInitializationActionsEx(matchType, countryId);
      this.store.dispatch(LoadMatchSubTypesAction({payload: matchType}))
    }

    const matchTypeControl = this.recordsForm.get('matchType')
    const matchSubTypeControl = this.recordsForm.get('matchSubType')
    const countriesSubControl = this.recordsForm.get('hostCountryId')

    this.matchTypeControlSub = matchTypeControl?.valueChanges.subscribe(
      value => {
        if (value != '' && value != undefined) {
          // only dispatch the action if the data has actually changed
          if (matchType != value) {
            // update the associated variable
            matchType = value
            // remember to reset the match subtype as well
            matchSubType = value
            startDate = ''
            endDate = ''
            season = ''
            // and update this in the actual form
            this.setFormDataForMatchType(matchSubType)
            this.dispatchInitializationActions(value);
            this.dispatchInitializationActionsEx(matchType, countryId);
            this.store.dispatch(LoadMatchSubTypesAction({payload: value}))
          }
        }
      }
    )

    this.matchSubTypeControlSub = matchSubTypeControl?.valueChanges.subscribe(
      value => {
        if (value != '' && value != undefined) {
          // only dispatch the action if the data has actually changed
          if (matchSubType != value) {
            // update the associated variable
            matchSubType = value
            this.dispatchInitializationActions(value);
            this.dispatchInitializationActionsEx(matchType, countryId);
          }
        }
      }
    )

    this.countriesControlSub = countriesSubControl?.valueChanges.subscribe(
      value => {
        if (value != '' && value != undefined) {
          // only dispatch the action if the data has actually changed
          if (matchSubType != value) {
            // update the associated variable
            matchSubType = value
            this.dispatchInitializationActionsEx(matchType, value);
          }
        }
      }
    )

    this.matchDateSub = this.matchDates$.subscribe((s: Array<MatchDate>) => {
      if (typeof startDate !== 'string') {
        // @ts-ignore
        startDate = `${startDate.getFullYear()}-${startDate.getMonth() + 1}-${startDate.getDate()}`
      }
      if (typeof endDate !== 'string') {
        // @ts-ignore
        endDate = `${endDate.getFullYear()}-${endDate.getMonth() + 1}-${endDate.getDate()}`
      }
      if (startDate != '') {
        this.recordsForm.patchValue({'startDate': startDate, 'endDate': endDate})
      } else {
        this.recordsForm.patchValue({'startDate': s[0]?.date, 'endDate': s[1]?.date})
      }
    });

    this.seriesDateSub = this.seriesDates$.subscribe((s: Array<string>) => {
      if (season != '') {
        this.recordsForm.patchValue({'season': season})
      } else {
        this.recordsForm.patchValue({'season': 'All Seasons'})
      }
    });

    this.formState$.subscribe((fs: FindFormState) => {
      if (fs.matchType != '') {
        this.recordsForm.patchValue(fs)
      }
    });

    // @ts-ignore
    this.filteredGrounds$ = this.recordsForm.controls['groundId'].valueChanges.pipe(
      startWith<string | Ground>(''),
      map(value => typeof value === 'string' ? value : value.knownAs),
      switchMap(name => name ? this.filterGrounds(name) : this.grounds$)
    )

  }

  ngOnDestroy() {
    this.countriesControlSub?.unsubscribe()
    this.matchTypeControlSub?.unsubscribe();
    this.matchSubTypeControlSub?.unsubscribe();
    this.seriesDateSub?.unsubscribe();
    this.matchDateSub?.unsubscribe()
  }

  public reset() {
    this.store.dispatch(SaveRecordsFormAction({
      payload: {
        matchType: this.defaultMatchType,
        matchSubType: '',
        teamId: 0,
        opponentsId: 0,
        groundId: 0,
        hostCountryId: 0,
        homeVenue: '',
        awayVenue: '',
        neutralVenue: '',
        startDate: '',
        endDate: '',
        season: '0',
        matchWon: 0,
        matchLost: 0,
        matchDrawn: 0,
        matchTied: 0,
        limit: this.initialLimit,
        startRow: '0',
        pageSize: 50,
        format: 1,
        isTeamBattingRecord: true
      }
    }))
    this.dispatchInitializationActions(this.defaultMatchType);
    this.dispatchInitializationActionsEx(this.defaultMatchType, 0);
    this.store.dispatch(LoadMatchSubTypesAction({payload: ''}))
    this.store.dispatch(LoadMatchSubTypesAction({payload: this.defaultMatchType}))
    return false
  }

  find() {
    if (this.limit?.errors?.['required']) {
      return
    }

    let route = ''
    switch (this.recordsForm.get('format')?.value) {
      case 1:
        route = `/records/${this.urlRoot}/overall`
        break;
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
      case 109:
      case 110:
        route = `/records/${this.urlRoot}/inningsbyinningsforwicket`
        break;
      case 2:
        route = `/records/${this.urlRoot}/inningsbyinnings`
        break;
      case 3:
        route = `/records/${this.urlRoot}/matchtotals`
        break;
      case 4:
        route = `/records/${this.urlRoot}/seriesaverages`
        break;
      case 5:
        route = `/records/${this.urlRoot}/groundaverages`
        break;
      case 6:
        route = `/records/${this.urlRoot}/bycountry`
        break;
      case 7:
        route = `/records/${this.urlRoot}/byopposition`
        break;
      case 8:
        route = `/records/${this.urlRoot}/byyearofmatchstart`
        break;
      case 9:
        route = `/records/${this.urlRoot}/byseason`
        break;
      case 10:
        route = `/records/${this.urlRoot}/bymatchresults`
        break;
      case 11:
        route = `/records/${this.urlRoot}/byextrasoverall`
        break;
      case 12:
        route = `/records/${this.urlRoot}/byextrasinnings`
        break;
      case 13:
        route = `/records/${this.urlRoot}/highesttargetchased`
        break;
      case 14:
        route = `/records/${this.urlRoot}/lowesttargetdefended`
        break;
      case 15:
        route = `/records/${this.urlRoot}/lowestunreducedtargetdefended`
        break;
    }

    let maybeStartDate = this.recordsForm.get('startDate')?.value
    let maybeEndDate = this.recordsForm.get('endDate')?.value

    let startD = ""
    if (maybeStartDate == null) {
      startD = '1700-01-01'
    } else {
      if (typeof maybeStartDate === 'string') {
        startD = maybeStartDate
      } else {
        // @ts-ignore
        // format the month with a leading 0 if necessary
        startD = `${maybeStartDate.getFullYear()}-${formatNumber(maybeStartDate.getMonth() + 1, "en", '2.0-0')}-${formatNumber(maybeStartDate.getDate(), "en", '2.0-0')}`
      }
    }

    let endD = ""
    if (maybeEndDate == null) {
      endD = '1700-01-01'
    } else {
      if (typeof maybeEndDate === 'string') {
        endD = maybeEndDate
      } else {
        // @ts-ignore
        endD = `${maybeEndDate.getFullYear()}-${formatNumber(maybeEndDate.getMonth() + 1, "en", '2.0-0')}-${formatNumber(maybeEndDate.getDate(), "en", '2.0-0')}`
      }
    }

    let startDate = DateTime.fromISO(startD, {zone: 'utc'}).toSeconds()
    let endDate = DateTime.fromISO(endD, {zone: 'utc'}).toSeconds()

    let sortOrder = this.recordsForm.get('sortOrder') ? this.recordsForm.get('sortOrder')?.value : this.defaultSortOrder; // runs
    let sortDirection = this.recordsForm.get('sortDirection') ? this.recordsForm.get('sortDirection')?.value : this.defaultSortDirection;


    let fivesLimit = this.recordsForm.get('matchType')?.value == 'tt'
    || this.recordsForm.get('matchType')?.value == 'itt'
    || this.recordsForm.get('matchType')?.value == 'wtt'
    || this.recordsForm.get('matchType')?.value == 'witt'
    || this.recordsForm.get('matchType')?.value == 'a'
    || this.recordsForm.get('matchType')?.value == 'wa'
    || this.recordsForm.get('matchType')?.value == 'o'
    || this.recordsForm.get('matchType')?.value == 'wo' ? 4 : 5

    let ground = this.recordsForm.get('groundId')?.value
    let groundId = ground == undefined ? 0 : ground.id

    let isTeamBattingRecord = null
    if (this.urlRoot?.includes("team")) {
      isTeamBattingRecord = this.recordsForm.get('isTeamBattingRecord')?.value
    }

    let qp = {
      matchType: this.recordsForm.get('matchType')?.value
      , matchSubType: this.recordsForm.get('matchSubType')?.value
      , teamId: this.recordsForm.get('teamId')?.value
      , opponentsId: this.recordsForm.get('opponentsId')?.value
      , groundId: groundId
      , hostCountryId: this.recordsForm.get('hostCountryId')?.value
      , homeVenue: this.recordsForm.get('homeVenue')?.value
      , awayVenue: this.recordsForm.get('awayVenue')?.value
      , neutralVenue: this.recordsForm.get('neutralVenue')?.value
      , sortOrder
      , sortDirection
      , startDate: startDate.toString()
      , endDate: endDate.toString()
      , season: this.recordsForm.get('season')?.value
      , matchWon: this.recordsForm.get('matchWon')?.value
      , matchLost: this.recordsForm.get('matchLost')?.value
      , matchDrawn: this.recordsForm.get('matchDrawn')?.value
      , matchTied: this.recordsForm.get('matchTied')?.value
      , limit: this.recordsForm.get('limit')?.value
      , startRow: '0'
      , pageSize: this.recordsForm.get('pageSize')?.value
    }

    let queryParams = {...qp}
    if (isTeamBattingRecord != null) {
      (queryParams as any).isTeamBattingRecord = isTeamBattingRecord
    }

    if (this.urlRoot.toLowerCase() == 'bowling') {
      // @ts-ignore
      queryParams['fivesLimit'] = fivesLimit
    }

    if (this.urlRoot.toLowerCase() == 'teams') {
      // @ts-ignore
      queryParams['isTeamBattingRecord'] = this.recordsForm.get('isTeamBattingRecord')?.value
    }

    // @ts-ignore
    if (this.recordsForm.get("isForWicket")?.value == true) {
      (queryParams as any)['partnershipWicket'] = this.recordsForm.get('format')?.value - 100
    }

    this.store.dispatch(SaveRecordsFormAction({
      payload:
        this.recordsForm.getRawValue()
    }))

    this.router.navigate([route], {queryParams})
  }

  isNotFirstClass() {
    return this.formHelperService.isNotMultiDay(this.recordsForm.get('matchType')?.value)
  }

  isNotInternational() {
    let matchType = this.recordsForm.get('matchType')?.value;

    return this.formHelperService.isNotInternational(matchType)
  }

  isInternational() {
    let matchType = this.recordsForm.get('matchType')?.value;

    return this.formHelperService.isInternational(matchType)
  }

  setSort(sortOrder: number, sortDirection: string) {
    this.recordsForm.get('sortOrder')?.setValue(sortOrder)
    this.recordsForm.get('sortDirection')?.setValue(sortDirection)
  }

  setLimit(limit: number) {
    this.recordsForm.get('limit')?.setValue(limit)
  }

  getLabelForDraw() {
    let matchType = this.getMatchTypeForForm();
    if (matchType == 'f' || matchType == 'wf' || matchType == 't' || matchType == 'wt')
      return 'Drawn'

    return 'No Result'
  }

  displayGroundsFn(ground?: Ground[]): string {
    return ground && ground.length == 1 ? ground[0].knownAs : '';
  }

  private initializeFormFromStore(): { teamId: number; opponentsId: number } {
    this.store.select(s => s.formState)
      .pipe(take(1))
      .subscribe((f: FindFormState) => this.findRecords = f);

    let teamId = (this.findRecords?.teamId != undefined) ? this.findRecords.teamId : 0
    let opponentsId = (this.findRecords?.opponentsId != undefined) ? this.findRecords.opponentsId : 0

    return {teamId, opponentsId}
  }

  private filterGrounds(knownAs: string): Observable<Ground[]> {
    return this.grounds$.pipe(
      map(g => g.filter(
        option => {
          return option.knownAs.toLowerCase().includes(knownAs.toLowerCase())
            || option.countryName.toLowerCase().includes(knownAs.toLowerCase());
        }
      ))
    )
  }

  private dispatchInitializationActions(matchType: string) {
    this.store.dispatch(LoadSeriesDatesAction({payload: matchType}))
    this.store.dispatch(LoadMatchDatesAction({payload: matchType}))
  }

  private dispatchInitializationActionsEx(matchType: string, countryId: number) {
    this.store.dispatch(LoadCountriesAction({payload: matchType}))
    this.store.dispatch(LoadTeamsAction({
      payload: {
        "matchType": matchType,
        "countryId": countryId
      }
    }))
    this.store.dispatch(LoadGroundsAction({
      payload: {
        "matchType": matchType,
        "countryId": countryId
      }
    }))

  }

  private setFormDataForMatchType(matchSubType: string) {
    this.recordsForm.patchValue({
      'matchSubType': matchSubType
      , teamId: 0
      , opponentsId: 0
      , groundId: defaultGround()
      , hostCountryId: 0

    })
  }

  private getMatchTypeForForm() {
    return (this.findRecords?.matchType != undefined && this.findRecords?.matchType !== '') ?
      this.findRecords?.matchType
      : this.defaultMatchType;

  }

  complete(evt: AutoCompleteCompleteEvent) {
    this.filteredGrounds$ = this.filterGrounds(evt.query)
  }

  filteredGrounds: Array<Ground> = []

  select($event: AutoCompleteSelectEvent) {

  }

}
