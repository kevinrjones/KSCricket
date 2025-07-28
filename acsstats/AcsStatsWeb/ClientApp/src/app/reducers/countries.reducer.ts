import {Country} from '../models/country.model'
import * as countryActions from './../actions/countries.actions'
import {createReducer, on} from '@ngrx/store';


export const initialState: ReadonlyArray<Country> = [];
export const countryReducer = createReducer(
  initialState,
  on(countryActions.LoadCountriesSuccessAction, (state, countries) => {
    return countries.payload
  })
);

