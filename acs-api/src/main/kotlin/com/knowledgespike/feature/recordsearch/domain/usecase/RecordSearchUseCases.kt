package com.knowledgespike.feature.recordsearch.domain.usecase

/**
 * A container class for various use cases related to record searches in the context of competitions,
 * countries, teams, grounds, series dates, and tournaments. Each use case is dedicated to a specific
 * aspect of the search functionality.
 *
 * This class serves as an aggregator for all the individual use case instances, allowing a unified
 * interface for managing and invoking search-related operations.
 *
 * @property getTeamsAndGroundsForCompetitionAndCountry Use case to retrieve teams and grounds for a specific competition and country.
 * @property getTeamsForCompetitionAndCountry Use case to retrieve teams for a specific competition and country.
 * @property getGroundsForCompetitionAndCountry Use case to retrieve grounds for a specific competition and country.
 * @property getCountriesForCompetition Use case to retrieve countries participating in a competition.
 * @property getSeriesDatesCompetition Use case to retrieve series dates associated with a competition.
 * @property getSeriesDatesForMatchTypes Use case to retrieve series dates for specific match types.
 * @property getTournamentsForSeason Use case to retrieve tournaments for a specific season and match types.
 * @property getStartAndEndDatesCompetition Use case to retrieve the start and end dates for a competition.
 * @property findMatches Use case to search for matches based on specific criteria.
 * @property getMatchesForTournaments Use case to fetch matches for a given tournament.
 */
data class RecordSearchUseCases(
    val getTeamsAndGroundsForCompetitionAndCountry: GetTeamsAndGroundsForCompetitionAndCountryUseCase,
    val getTeamsForCompetitionAndCountry: GetTeamsForCompetitionAndCountryUseCase,
    val getGroundsForCompetitionAndCountry: GetGroundsForCompetitionAndCountryUseCase,
    val getCountriesForCompetition: GetCountriesForCompetitionUseCase,
    val getSeriesDatesCompetition: GetSeriesDatesForCompetitionUseCase,
    val getSeriesDatesForMatchTypes: GetSeriesDatesForMatchTypesUseCase,
    val getTournamentsForSeason: GetTournamentsForSeason,
    val getStartAndEndDatesCompetition: GetStartAndEndDatesForCompetitionUseCase,
    val findMatches: FindMatchesUseCase,
    val getMatchesForTournaments: GetMatchesForTournamentUseCase
)
