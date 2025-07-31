package com.knowledgespike.feature.recordsearch.data.repository

import arrow.core.Either
import com.knowledgespike.DIALECT
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.values.CountryId
import com.knowledgespike.core.type.values.MatchType
import com.knowledgespike.feature.recordsearch.domain.model.*
import com.knowledgespike.feature.recordsearch.domain.repository.MainSearchRepository
import com.knowledgespike.plugins.DataSource
import org.jooq.impl.DSL.using

/**
 * Repository implementation for performing search operations using Jooq.
 * This class interacts with the database to handle various queries related to match types,
 * competitions, grounds, teams, countries, series dates, and tournaments.
 *
 * @constructor Initializes the repository with the provided data source.
 * @property dataSource A wrapper around a HikariCP data source used for database connectivity.
 */
class JooqMainSearchRepository(private val dataSource: DataSource) : MainSearchRepository {
    /**
     * Fetches a list of match types by querying the underlying database.
     *
     * @return A list of MatchTypeEntity objects representing the match types retrieved from the database.
     */
    override fun getMatchTypes(): List<MatchTypeEntity> {
        val context = using(dataSource.dataSource, DIALECT)

        return JooqSearch.getMatchTypes(context)
    }

    /**
     * Retrieves a list of competitions based on the specified match type.
     *
     * @param matchType The type of match used to filter the competitions.
     * @return A list of competitions that match the specified match type.
     */
    override fun getCompetitions(matchType: MatchType): List<Competition> {
        val context = using(dataSource.dataSource, DIALECT)
        return JooqSearch.getCompetitions(context, matchType)
    }

    /**
     * Retrieves a list of grounds for a specific match type and country.
     *
     * @param matchType The type of match used to filter the grounds.
     * @param countryId The identifier of the country to filter the grounds. If set to 0, grounds across all countries are included.
     * @return A list of grounds matching the given match type and country, including an additional entry for "All Grounds" at the start.
     */
    override fun getGroundsForCompetitionAndCountry(
        matchType: MatchType,
        countryId: CountryId,
    ): List<Ground> {
        val context = using(dataSource.dataSource, DIALECT)
        return JooqSearch.getGroundsForCompetitionAndCountry(context, matchType, countryId)
    }

    /**
     * Retrieves a list of countries associated with the specified match type.
     * This includes all distinct countries where the given match type is played,
     * ordered by country name. Additionally, a default country representing
     * "All Countries" is included in the list.
     *
     * @param matchType The match type for which countries need to be retrieved.
     * @return A list of countries associated with the specified match type,
     * including a default entry for "All Countries".
     */
    override fun getCountriesForCompetition(matchType: MatchType): List<Country> {
        val context = using(dataSource.dataSource, DIALECT)
        return JooqSearch.getCountriesForCompetition(context, matchType)
    }

    /**
     * Retrieves a list of series dates for a specified match type, including a predefined "All Seasons" entry.
     *
     * @param matchType The type of match used to filter and identify relevant series dates.
     * @return A list of series dates as strings, with "All Seasons" included as the first entry.
     */
    override fun getSeriesDatesForCompetition(matchType: MatchType): List<String> {
        val context = using(dataSource.dataSource, DIALECT)
        return JooqSearch.getSeriesDateForCompetition(context, matchType)
    }

    /**
     * Retrieves a mapping of series dates grouped by decades for the specified match types.
     *
     * This method interacts with the database to fetch a list of series dates associated with the provided
     * match types. The resulting data is grouped by their respective decades into a HashMap, where the keys
     * are decade values (e.g., 1990, 2000) and the values are lists of series date strings that belong to those decades.
     * The data is fetched in ascending order of the series dates.
     *
     * @param matchTypes A list of MatchType objects representing the types of matches to filter the series dates by.
     * @return An Either containing:
     *         - A HashMap where the keys are decades (as integers) and the values are lists of series dates as strings,
     *           on successful retrieval and grouping.
     *         - An Error if a failure occurs during data processing.
     */
    override fun getSeriesDatesForMatchTypes(matchTypes: List<MatchType>): Either<Error, HashMap<Int, List<String>>> {
        val context = using(dataSource.dataSource, DIALECT)
        return JooqSearch.getSeriesDateForMatchTypes(context, matchTypes)
    }

    /**
     * Retrieves a list of tournament names for a specific season and match types.
     *
     * @param season The season for which tournaments are to be fetched, represented as a string.
     * @param matchTypes A list of `MatchType` objects representing the types of matches to filter tournaments.
     * @return An `Either` containing an `Error` in case of failure, or a list of tournament names (`List<String>`) if successful.
     */
    override fun getTournamentsForSeason(season: String, matchTypes: List<MatchType>): Either<Error, List<String>> {
        val context = using(dataSource.dataSource, DIALECT)
        return JooqSearch.getTournamentsForSeason(context, season, matchTypes)
    }

    /**
     * Retrieves the list of matches for a specific tournament.
     *
     * @param tournament The name of the tournament for which matches are being fetched.
     * @return Either an instance of Error in case of failure, or a List of MatchListDto
     * containing the details of the matches for the specified tournament.
     */
    override fun getMatchesForTournament(tournament: String): Either<Error, List<MatchListDto>> {
        val context = using(dataSource.dataSource, DIALECT)
        return JooqSearch.getMatchesForTournament(context, tournament)
    }

    /**
     * Finds matches based on the given search parameters and returns either a list of matches
     * or an error if the search operation fails.
     *
     * @param parameters ValidatedMatchSearchParameters object containing search criteria such as:
     * - Teams (home and away), with options for exact or partial matching
     * - Date range (start and end date for matches)
     * - Venue identifiers
     * - Match result filters (e.g., win, loss, draw)
     * - Match type (e.g., test, ODI, T20)
     * @return Either an Error object if the search fails, or a list of MatchListDto objects
     * representing the matches that meet the given criteria.
     */
    override fun findMatches(parameters: ValidatedMatchSearchParameters): Either<Error, List<MatchListDto>> {
        val context = using(dataSource.dataSource, DIALECT)
        return JooqFindMatches.findMatches(context, parameters)
    }


    /**
     * Retrieves the start and end dates for a competition based on the specified match type.
     *
     * This method queries the underlying data source within a given database context to
     * fetch the earliest and latest match dates for the provided match type. The start
     * and end dates are returned as a list of `MatchDate` objects.
     *
     * @param matchType The type of match for which the start and end dates are required.
     *                  It determines the scope of the competition being queried.
     * @return A list of `MatchDate` objects where the first element represents the start date
     *         and the second element represents the end date of the competition for the specified match type.
     */
    override fun getStartAndEndDatesForCompetition(matchType: MatchType): List<MatchDate> {
        val context = using(dataSource.dataSource, DIALECT)
        return JooqSearch.getStartAndEndDatesForCompetition(context, matchType)
    }

    /**
     * Retrieves a list of teams for a given competition type and country.
     *
     * The method fetches data by interacting with the underlying data source and
     * returns a list of teams associated with the specified match type and country.
     *
     * @param matchType The type of the match, represented as a `MatchType`, used to filter teams.
     * @param countryId The identifier of the country, represented as a `CountryId`, used to filter teams.
     * @return A list of `Team` objects that are associated with the given match type and country.
     */
    override fun getTeamsForCompetitionAndCountry(
        matchType: MatchType,
        countryId: CountryId,
    ): List<Team> {
        val context = using(dataSource.dataSource, DIALECT)

        return JooqSearch.getTeamsForCompetitionAndCountry(context, matchType, countryId)
    }

}