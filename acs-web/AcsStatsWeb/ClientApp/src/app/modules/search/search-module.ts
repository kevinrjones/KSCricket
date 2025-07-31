import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from '@angular/router';
import {ReactiveFormsModule} from '@angular/forms';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {MainSearchComponent} from "./components/main-search/mainsearch.component";
import {SharedModule} from "../shared/shared.module";
import {ScorecardSearchComponent} from "./components/scorecard-search/scorecardsearch.component";
import {PlayerSearchComponent} from "./components/playerSearch/playersearch.component";
import {StoreModule} from "@ngrx/store";
import {loadPlayerSearchFormReducer, loadScorecardSearchFormReducer} from "./reducers/form-state.reducer";


const routes: Routes = [
  {path: 'search', component: MainSearchComponent},
  {path: 'search/card', component: MainSearchComponent},
  {path: 'search/player', component: MainSearchComponent},
];


@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
    StoreModule.forFeature('search', {
      scorecardSearch: loadScorecardSearchFormReducer,
      playerSearch: loadPlayerSearchFormReducer,
    }),
    FontAwesomeModule,
    SharedModule
  ],
  exports: [RouterModule, MainSearchComponent, ScorecardSearchComponent, PlayerSearchComponent],
  declarations: [MainSearchComponent, ScorecardSearchComponent, PlayerSearchComponent]
})
export class SearchModule {
}
