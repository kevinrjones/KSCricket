package com.knowledgespike.feature.recordsearch.di

import com.knowledgespike.feature.recordsearch.data.repository.JooqMainSearchRepository
import com.knowledgespike.feature.recordsearch.domain.repository.MainSearchRepository
import com.knowledgespike.feature.recordsearch.domain.usecase.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


/**
 * Dependency injection module that provides the main components and use cases
 * related to the search functionality within the application.
 *
 * This module includes bindings for various use cases responsible for handling
 * operations such as retrieving grounds, teams, matches, tournaments, dates,
 * and countries based on different search criteria. It also defines the
 * main search repository interface implementation used by these use cases.
 *
 * Bindings included in this module:
 * - Use cases for retrieving specific data related to competitions, match types,
 *   countries, teams, grounds, and tournaments.
 * - Composite use cases aggregating multiple related use cases.
 * - Repository implementation for interacting with the main search data layer.
 */
val mainSearchModule = module {
    singleOf(::GetGroundsForCompetitionAndCountryUseCase)
    singleOf(::GetCountriesForCompetitionUseCase)
    singleOf(::GetSeriesDatesForCompetitionUseCase)
    singleOf(::GetSeriesDatesForMatchTypesUseCase)
    singleOf(::GetTournamentsForSeason)
    singleOf(::GetStartAndEndDatesForCompetitionUseCase)
    singleOf(::GetTeamsAndGroundsForCompetitionAndCountryUseCase)
    singleOf(::GetMatchesForTournamentUseCase)
    singleOf(::FindMatchesUseCase)
    singleOf(::GetTeamsForCompetitionAndCountryUseCase)
    singleOf(::RecordSearchUseCases)
    singleOf(::JooqMainSearchRepository) bind MainSearchRepository::class
}
