import {Team} from './team.model'
import {Country} from './country.model'
import {Ground} from './ground.model'
import {MatchDate} from './date.model';
import {MatchSubTypeModel} from './match-sub-type.model';
import {ErrorDetails} from './error.model';
import {FindFormState} from './find-form.state';

export interface AppState {
  teams: Team[],
  countries: Country[],
  grounds: Ground[],
  seriesDates: string[],
  matchDates: MatchDate[],
  matchSubTypes: MatchSubTypeModel[],
  formState: FindFormState,
  errorState: ErrorDetails,
  loading: boolean
}
