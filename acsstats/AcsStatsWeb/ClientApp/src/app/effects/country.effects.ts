import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, map, mergeMap} from 'rxjs/operators';
import {of} from 'rxjs';
import {CountrySearchService} from '../services/countrysearch.service';
import {LoadCountriesAction, LoadCountriesSuccessAction} from '../actions/countries.actions';
import {createError} from '../helpers/ErrorHelper';
import {RaiseErrorAction} from '../actions/error.actions';
import {Envelope} from '../models/envelope';
import {Country} from '../models/country.model';

@Injectable()
export class CountryEffects {
  // todo: in the map check that the envelope is valid and if not return an error
  loadCountries$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LoadCountriesAction),
      mergeMap(action => this.countrySearchService.findCountriesForMatchType(action.payload)
        .pipe(
          map((countries: Envelope<Country[]>) => {

              if (countries.errorMessage != null && countries.errorMessage != '')
                return RaiseErrorAction({payload: createError(2, countries.errorMessage)})

              return LoadCountriesSuccessAction({payload: countries.result})

            }
          ),
          catchError((err) => of(RaiseErrorAction({payload: createError(1, 'Unable to get countries')})))
        ))
    );
  });

  constructor(
    private countrySearchService: CountrySearchService
    , private actions$: Actions
  ) {
  }

}
