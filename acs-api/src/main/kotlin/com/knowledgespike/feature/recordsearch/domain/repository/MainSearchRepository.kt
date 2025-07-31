package com.knowledgespike.feature.recordsearch.domain.repository

import arrow.core.Either
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.values.CountryId
import com.knowledgespike.core.type.values.MatchType
import com.knowledgespike.feature.recordsearch.domain.model.*

/**
 * MainSearchRepository defines a collection of methods for querying and retrieving cricket-related data,
 * such as match types, competitions, countries, grounds, teams, series dates, tournaments, and match details.
 */
interface MainSearchRepository {
    /**
     * Retrieves a list of match types available in the system.
     *
     * This method returns a list of `MatchTypeEntity` objects, each representing
     * a specific match type along with its associated metadata such as an identifier,
     * type, and description.
     *
     * @return A list of `MatchTypeEntity` instances, where each object describes a match type.
     */
    fun getMatchTypes(): List<MatchTypeEntity>
    /**
     * Retrieves a list of competitions for the specified match type.
     *
     * @param matchType The type of match for which competitions are to be retrieved.
     * @return A list of competitions corresponding to the provided match type.
     */
    fun getCompetitions(matchType: MatchType): List<Competition>
    /**
     * Retrieves a list of grounds for a given match type and country.
     *
     * @param matchType The type of match for which grounds are to be fetched.
     * @param countryId The identifier of the country for which grounds data is needed.
     * @return A list of grounds that match the specified match type and country.
     */
    fun getGroundsForCompetitionAndCountry(matchType: MatchType, countryId: CountryId): List<Ground>
    /**
     * Retrieves a list of countries that are eligible for a competition based on the given match type.
     *
     * @param matchType The type of match for which the list of countries participating in the competition is required.
     * @return A list of countries associated with the specified match type.
     */
    fun getCountriesForCompetition(matchType: MatchType): List<Country>
    /**
     * Retrieves a list of series dates for a given match type during a competition.
     *
     * @param matchType The type of match for which the series dates are to be retrieved.
     * @return A list of strings representing the series dates for the specified match type.
     */
    fun getSeriesDatesForCompetition(matchType: MatchType): List<String>
    /**
     * Fetches a mapping of match type IDs to their corresponding list of series dates.
     *
     * This method takes a list of `MatchType` objects and returns an `Either` result containing
     * either an error or a `HashMap` where the keys are match type IDs and the values are lists
     * of series dates in string format. This can be used to associate match types with their
     * respective series schedules.
     *
     * @param matchTypes A list of `MatchType` objects representing the match types to retrieve series dates for.
     * @return An `Either<Error, HashMap<Int, List<String>>>`, where the left value is an `Error` object in case of failure,
     * and the right value is a mapping of match type IDs to lists of series dates.
     */
    fun getSeriesDatesForMatchTypes(matchTypes: List<MatchType>): Either<Error, HashMap<Int, List<String>>>
    /**
     * Retrieves the start and end dates for competitions of a specified match type.
     *
     * This function queries the competition data to determine the relevant date range
     * for the specified match type and returns a list of `MatchDate` objects representing
     * the start and end dates.
     *
     * @param matchType The type of match for which the start and end dates are to be retrieved.
     * @return A list of `MatchDate` objects, each containing date details (start or end) for the specified competition.
     */
    fun getStartAndEndDatesForCompetition(matchType: MatchType): List<MatchDate>
    /**
     * Retrieves a list of teams for a given competition and country.
     *
     * This method fetches teams that are part of a specific competition
     * for a provided match type and country identifier.
     *
     * @param matchType The type of match for which the teams are to be retrieved.
     *                  It determines the competition's match classification.
     * @param countryId The unique identifier for the country whose teams are to be retrieved.
     * @return A list of teams associated with the specified match type and country.
     */
    fun getTeamsForCompetitionAndCountry(matchType: MatchType, countryId: CountryId): List<Team>
    /**
     * Retrieves a list of tournaments for the specified season and match types.
     *
     * @param season The season for which tournaments are to be fetched, represented as a string.
     * @param matchTypes A list of MatchType instances specifying the types of matches for which tournaments are to be retrieved.
     * @return An Either instance containing an Error in case of failure or a list of tournament names (as strings) if successful.
     */
    fun getTournamentsForSeason(season: String, matchTypes: List<MatchType>): Either<Error, List<String>>
    /**
     * Fetches the list of matches associated with a specific tournament.
     *
     * This method retrieves match details such as teams, location, match date,
     * and other related information for all matches in the specified tournament.
     *
     * @param tournament The name of the tournament for which matches need to be fetched.
     *                   It is expected to be a valid tournament name.
     * @return An `Either` containing either an `Error` object in case of a failure
     *         or a list of `MatchListDto` representing the match details
     *         of the specified tournament if the operation succeeds.
     */
    fun getMatchesForTournament(tournament: String): Either<Error, List<MatchListDto>>
    /**
     * Finds and retrieves a list of matches based on the specified search parameters.
     *
     * This method queries the repository for match details that match the provided
     * search criteria encapsulated in a `ValidatedMatchSearchParameters` object. The results
     * are either a list of `MatchListDto` objects or an `Error` in case of a failure.
     *
     * @param parameters The validated parameters used to filter the matches. Includes details
     * such as teams, date range, venue, match result, and type of match.
     * @return Either an `Error` if the operation fails, or a `List` of `MatchListDto` objects
     * containing the details of the matches that match the given search criteria.
     */
    fun findMatches(parameters: ValidatedMatchSearchParameters): Either<Error, List<MatchListDto>>
}