import {Country} from '../models/country.model';
import {createAction, props} from '@ngrx/store'


export const LOAD_COUNTRIES = 'LOAD_COUNTRIES;';
export const LOAD_COUNTRIES_SUCCESS = 'LOAD_COUNTRIES_SUCCESS;';


export const LoadCountriesAction = createAction(LOAD_COUNTRIES, props<{ payload: string }>())
export const LoadCountriesSuccessAction = createAction(LOAD_COUNTRIES_SUCCESS, props<{ payload: Country[] }>())

