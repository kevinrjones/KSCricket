import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {SortOrder} from '../../../../models/sortorder.model';
import {Store} from "@ngrx/store";
import {SearchState} from "../../models/app-state";
import {take} from "rxjs/operators";
import {FindPlayerState} from "../../models/find-player.models";
import {SavePlayerSearchFormAction} from "../../actions/search-state.actions";

@Component({
    selector: 'app-player-search',
    templateUrl: './playersearch.component.html',
    styleUrls: ['./playersearch.component.css'],
    standalone: false
})
export class PlayerSearchComponent {
  type: string = '';
  playerClass: string = 'tab-primary';
  cardClass: string = 'tab-secondary';

  playerSearchForm!: FormGroup;
  nameMessage: String = '';

  importedSortOrder = SortOrder;

  private validationMessages = {
    required: 'Please enter a name',
    minlength: 'The name must be at least 3 characters'
  }

  private playerState!: FindPlayerState | undefined;

  constructor(private fb: FormBuilder,
              private router: Router,
              private store: Store<SearchState>) {
  }

  ngOnInit() {

    this.store.select(s => s)
      .pipe(take(1))
      .subscribe((f: SearchState) => this.playerState = f.search.playerSearch);

    let name = (this.playerState?.name != undefined) ? this.playerState.name : ''
    let team = (this.playerState?.team != undefined) ? this.playerState.team : ''
    let exactMatch = (this.playerState?.exactMatch != undefined) ? this.playerState.exactMatch : false
    let startDate = (this.playerState?.startDate != undefined) ? this.playerState.startDate : ''
    let endDate = (this.playerState?.endDate != undefined) ? this.playerState.endDate : ''


    this.playerSearchForm = this.fb.group({
      name: [name, [Validators.required, Validators.minLength(3)]],
      exactMatch: exactMatch,
      team: team,
      startDate: startDate,
      endDate: endDate,
    });

    const nameControl = this.playerSearchForm.get('name')

    nameControl?.valueChanges.subscribe(
      _ => this.setMessage(nameControl)
    )

  }

  find() {

    this.router.navigate(['/playerlist'], {
      queryParams: {
        name: this.playerSearchForm.get('name')?.value
        , exactMatch: this.playerSearchForm.get('exactMatch')?.value
        , team: this.playerSearchForm.get('team')?.value
        , startDate: this.playerSearchForm.get('startDate')?.value
        , endDate: this.playerSearchForm.get('endDate')?.value
        , sortOrder: this.importedSortOrder.debutDate
        , sortDirection: "DESC"
      }
    })

    this.store.dispatch(SavePlayerSearchFormAction({
      payload:
        this.playerSearchForm.getRawValue()
    }))

  }


  private setMessage(nameControl: AbstractControl): void {
    this.nameMessage = ''

    if ((nameControl.touched || nameControl.dirty) && nameControl.errors) {
      this.nameMessage = Object.keys(nameControl.errors).map(
        key => (this.validationMessages as any)[key]).join(' ');
    }
  }
}
