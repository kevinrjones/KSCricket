import {createReducer, on} from '@ngrx/store';
import {Team} from '../models/team.model';
import {LoadTeamsSuccessAction} from '../actions/teams.actions';


export const initialByMatchTypeState: Team[] = [];
export const teamReducer = createReducer(
  initialByMatchTypeState,
  on(LoadTeamsSuccessAction, (state, teams) => {
    return teams.payload
  })
);

