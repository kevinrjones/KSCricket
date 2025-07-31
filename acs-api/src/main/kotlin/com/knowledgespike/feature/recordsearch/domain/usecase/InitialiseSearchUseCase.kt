package com.knowledgespike.feature.recordsearch.domain.usecase

import arrow.core.Either
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.shared.now
import com.knowledgespike.core.type.values.CountryId
import com.knowledgespike.core.type.values.MatchType
import com.knowledgespike.feature.recordsearch.domain.model.*
import com.knowledgespike.feature.recordsearch.domain.repository.MainSearchRepository
import kotlinx.datetime.LocalDate

/**
 * Use case for retrieving a combination of teams and grounds based on a specific match type
 * and a country identifier. This operates using the provided `MainSearchRepository` to fetch
 * the required data.
 *
 * @constructor Initializes the use case with the provided `MainSearchRepository` instance.
 *
 * @property repository An instance of `MainSearchRepository` used to access search operations
 * for fetching teams and grounds data.
 */
class GetTeamsAndGroundsForCompetitionAndCountryUseCase(private val repository: MainSearchRepository) {
    /**
     * Invokes the use case to retrieve search data based on the given match type and country.
     *
     * @param matchType The type of match to filter the search results.
     * @param country The country identifier to filter the search results.
     * @return A `SearchData` object containing the teams and grounds corresponding to the specified match type and country.
     */
    operator fun invoke(matchType: MatchType, country: CountryId): SearchData {
        return getTeamsAndGroundsForCountry(repository, matchType, country)
    }
}

/**
 * Use case class for retrieving a list of teams for a specific competition and country.
 *
 * This class provides a single-use functionality to fetch teams based on a given match type
 * and country identifier. It relies on an instance of `MainSearchRepository` to perform the
 * data fetching. The operator function makes this class callable like a function.
 *
 * @constructor Initializes the use case with the required repository instance.
 * @param repository The repository instance used for accessing team data.
 */
class GetTeamsForCompetitionAndCountryUseCase(private val repository: MainSearchRepository) {
    /**
     * Retrieves a list of teams based on the specified match type and country ID.
     *
     * @param matchType The type of match used to filter the teams.
     * @param country The identifier for the country used to filter the teams.
     * @return A list of teams matching the specified match type and country.
     */
    operator fun invoke(matchType: MatchType, country: CountryId): List<Team> {
        return getTeamsForCompetitionAndCountry(repository, matchType, country)
    }
}

/**
 * Use case for retrieving a list of cricket grounds based on a specified match type and country.
 *
 * This class leverages the `repository` to fetch data about grounds suitable for the given
 * combination of match type and country ID. It encapsulates the business logic required
 * to perform this operation, orchestrating calls to the repository layer.
 *
 * @param repository An instance of `MainSearchRepository`, which provides access
 * to the data source for grounds, match types, and related entities.
 */
class GetGroundsForCompetitionAndCountryUseCase(private val repository: MainSearchRepository) {
    /**
     * Invokes the method to retrieve a list of grounds based on the specified match type and country.
     *
     * @param matchType The type of match to filter the grounds.
     * @param country The country identifier for filtering the grounds.
     * @return A list of grounds matching the given match type and country.
     */
    operator fun invoke(matchType: MatchType, country: CountryId): List<Ground> {
        return getGroundsForCompetitionAndCountry(repository, matchType, country)
    }
}

/**
 * Use case to retrieve the series dates for a given competition.
 *
 * This class interacts with the `MainSearchRepository` to fetch series dates
 * specific to a provided `MatchType`. The operation is performed by invoking
 * the use case as a function.
 *
 * @constructor Creates an instance of the use case with the provided repository.
 * @param repository The repository used to access data related to series dates.
 */
class GetSeriesDatesForCompetitionUseCase(private val repository: MainSearchRepository) {
    /**
     * Invokes the function to retrieve a list of series dates for the given match type.
     *
     * @param matchType The type of match for which series dates are to be retrieved.
     * @return A list of series dates corresponding to the given match type.
     */
    operator fun invoke(matchType: MatchType): List<String> {
        return getSeriesDatesForCompetition(repository, matchType)
    }
}

/**
 * Use case for retrieving series dates based on a list of match types.
 *
 * This class handles the business logic to fetch series dates for the specified match types
 * using the provided repository. It returns either an error or a mapping of match type IDs
 * to their corresponding series dates.
 *
 * @constructor Creates an instance of the use case with a given repository.
 * @param repository The MainSearchRepository instance used for fetching series dates.
 */
class GetSeriesDatesForMatchTypesUseCase(private val repository: MainSearchRepository) {
    /**
     * Invokes the function to retrieve a mapping of match type IDs to their corresponding series dates.
     *
     * @param matchTypes A list of match types for which series dates are to be retrieved.
     * @return An `Either` containing either an `Error` object if the operation fails, or a `HashMap` mapping match type IDs to lists of series date strings if successful.
     */
    operator fun invoke(matchTypes: List<MatchType>): Either<Error, HashMap<Int, List<String>>> {
        return getSeriesDatesForMatchTypes(repository, matchTypes)
    }
}

/**
 * A use case class that retrieves the list of tournaments for a given season and match types.
 *
 * This class interacts with the `MainSearchRepository` to fetch tournament data based on the
 * specified season and match types.
 *
 * @param repository The repository instance responsible for accessing tournament-related data.
 */
class GetTournamentsForSeason(private val repository: MainSearchRepository) {
    /**
     * Operator function to retrieve a list of tournaments for a specified season and match types.
     *
     * @param season The season for which tournaments are to be retrieved.
     * @param matchTypes A list of match types to filter the tournaments.
     * @return An instance of Either containing an Error if retrieval fails,
     *         or a list of tournament names as Strings if successful.
     */
    operator fun invoke(season: String, matchTypes: List<MatchType>): Either<Error, List<String>> {
        return getTournamentsForSeasson(repository, season, matchTypes)
    }
}

/**
 * Use case to retrieve the start and end dates of competitions based on a given match type.
 *
 * This class is responsible for invoking the necessary methods from the repository to fetch
 * the dates associated with the specified match type. The result is a list of `MatchDate`
 * objects representing the relevant date range.
 *
 * @property repository The repository used to interact with data sources for retrieving competition dates.
 */
class GetStartAndEndDatesForCompetitionUseCase(private val repository: MainSearchRepository) {
    /**
     * Invokes the function to retrieve a list of match dates for a specific match type.
     *
     * @param matchType the type of match for which the dates are to be retrieved
     * @return a list of dates corresponding to the start and end dates for the specified match type
     */
    operator fun invoke(matchType: MatchType): List<MatchDate> {
        return getStartAndEndDatesForCompetition(repository, matchType)
    }
}

/**
 * Use case for finding matches based on validated search parameters.
 *
 * This class acts as a bridge between the domain layer and the underlying repository,
 * providing a business logic wrapper around the repository's `findMatches` method.
 * It utilizes a `MainSearchRepository` to delegate the search operation.
 *
 * @param repository The repository responsible for accessing match data.
 */
class FindMatchesUseCase(private val repository: MainSearchRepository) {
    /**
     * Invokes the function to find matches based on the provided validated match search parameters.
     *
     * @param parameters the validated search parameters for finding matches
     * @return either an error or a list of match list DTOs
     */
    operator fun invoke(parameters: ValidatedMatchSearchParameters): Either<Error, List<MatchListDto>> {
        return findMatches(repository, parameters)
    }
}

/**
 * Use case class responsible for retrieving match details for a given tournament.
 *
 * This class orchestrates the logic for fetching a list of matches associated with a specific tournament
 * by utilizing the `MainSearchRepository`. The match details are returned in the form of a `MatchListDto` list
 * wrapped in an `Either` to capture success or failure scenarios.
 *
 * @property repository The repository interface used for accessing match and tournament data.
 */
class GetMatchesForTournamentUseCase(private val repository: MainSearchRepository) {
    /**
     * Invokes the function to retrieve the list of matches for the specified tournament.
     *
     * @param tournament the name or identifier of the tournament for which matches are required.
     * @return an `Either` containing an `Error` in case of failure or a `List` of `MatchListDto` on success.
     */
    operator fun invoke(tournament: String): Either<Error, List<MatchListDto>> {
        return getMatchesForTournament(repository, tournament)
    }
}

/**
 * This use case retrieves a list of countries participating in a competition
 * for a specified match type.
 *
 * @constructor Initializes the use case with a repository that provides access to competition and country data.
 * @param repository An instance of MainSearchRepository used for retrieving country information based on match type.
 */
class GetCountriesForCompetitionUseCase(private val repository: MainSearchRepository) {
    /**
     * Retrieves a list of countries participating in a competition based on the specified match type.
     *
     * @param matchType Specifies the type of match to filter the competition countries.
     * @return A list of countries that participate in a competition for the given match type.
     */
    operator fun invoke(matchType: MatchType): List<Country> {
        return getCountriesForCompetition(repository, matchType)
    }
}


/**
 * Retrieves a list of countries that are eligible for a competition based on the specified match type.
 *
 * @param repository The repository from which the country data will be fetched.
 *                   It provides access to various methods for querying cricket-related data.
 * @param matchType The type of match (e.g., international, domestic) for which the eligible countries are required.
 *                  This determines the scope of the competition.
 * @return A list of countries associated with the given match type for the competition.
 */
fun getCountriesForCompetition(
    repository: MainSearchRepository,
    matchType: MatchType,
): List<Country> {
    val countries =

        repository.getCountriesForCompetition(matchType)
    return countries
}

/**
 * Retrieves a list of series dates for a specified competition and match type.
 *
 * @param repository The repository instance used to fetch data related to competitions.
 * @param matchType The type of match for which the series dates are to be retrieved.
 * @return A list of strings, where each string represents a series date for the specified match type.
 */
fun getSeriesDatesForCompetition(
    repository: MainSearchRepository,
    matchType: MatchType
): List<String> {
    val seriesDates = repository.getSeriesDatesForCompetition(matchType)

    return seriesDates
}

/**
 * Fetches a mapping of match type IDs to their corresponding lists of series dates.
 *
 * This function queries the provided repository to retrieve the series dates
 * for each match type in the input list and returns the results as an `Either`
 * containing an `Error` in case of failure or a mapping of match type IDs to lists
 * of series dates if successful.
 *
 * @param repository The repository used to fetch the series dates for the specified match types.
 * @param matchTypes A list of `MatchType` instances for which the series dates are to be retrieved.
 * @return An `Either` containing an `Error` object in case of failure, or a `HashMap` where
 *         the keys are match type IDs and the values are lists of series dates as strings.
 */
fun getSeriesDatesForMatchTypes(
    repository: MainSearchRepository,
    matchTypes: List<MatchType>
): Either<Error, HashMap<Int, List<String>>> {
    val seriesDates = repository.getSeriesDatesForMatchTypes(matchTypes)

    return seriesDates
}

/**
 * Fetches a list of tournament names for a given season and match types.
 *
 * @param repository The repository instance used to retrieve tournament data.
 * @param season A string representing the season for which tournaments are to be fetched.
 * @param matchTypes A list of MatchType objects specifying the types of matches for which tournaments should be retrieved.
 * @return An Either containing an Error on failure, or a list of tournament names (as strings) on success.
 */
fun getTournamentsForSeasson(
    repository: MainSearchRepository,
    season: String,
    matchTypes: List<MatchType>
): Either<Error, List<String>> {
    return repository.getTournamentsForSeason(season, matchTypes)
}

/**
 * Retrieves the start and end dates for competitions of a specified match type.
 *
 * This method interacts with the provided repository to obtain a list of match dates
 * that represent the start and end dates for competitions corresponding to the given match type.
 *
 * @param repository The repository instance used to fetch data related to cricket matches and competitions.
 * @param matchType The type of match for which the start and end dates are to be retrieved.
 * @return A list of `MatchDate` objects containing the start and end dates for the specified competition.
 */
fun getStartAndEndDatesForCompetition(
    repository: MainSearchRepository,
    matchType: MatchType,
): List<MatchDate> {
    LocalDate.now()
    val startEndDates =
        repository.getStartAndEndDatesForCompetition(matchType)
    return startEndDates
}

/**
 * Retrieves a list of grounds for a specific match type and country.
 *
 * This function calls the repository to fetch the grounds that match the provided
 * match type and country identifier.
 *
 * @param repository The repository instance used to access the data source.
 * @param matchType The type of match for which grounds are to be retrieved.
 * @param countryId The identifier of the country for which grounds data is needed.
 * @return A list of grounds that match the specified match type and country.
 */
fun getGroundsForCompetitionAndCountry(
    repository: MainSearchRepository,
    matchType: MatchType,
    countryId: CountryId,
): List<Ground> {
    val grounds =

        repository.getGroundsForCompetitionAndCountry(
            matchType,
            countryId
        )
    return grounds
}

/**
 * Retrieves the teams and grounds associated with a specific country and match type.
 *
 * This method combines the results of fetching teams and grounds for the provided
 * match type and country identifier, returning them as part of a unified `SearchData` object.
 *
 * @param repository The repository instance used to fetch teams and grounds data.
 *                   It provides access to methods for querying cricket-related information.
 * @param matchType The type of match for which related teams and grounds are to be retrieved.
 * @param countryId The unique identifier of the country for which teams and grounds are required.
 * @return A `SearchData` object containing lists of teams and grounds relevant
 *         to the specified match type and country.
 */
private fun getTeamsAndGroundsForCountry(
    repository: MainSearchRepository,
    matchType: MatchType,
    countryId: CountryId,
): SearchData {
    val teams = getTeamsForCompetitionAndCountry(repository, matchType, countryId)
    val grounds = getGroundsForCompetitionAndCountry(repository, matchType, countryId)

    return (SearchData(teams = teams, grounds = grounds))
}

/**
 * Retrieves a list of teams for a specific competition and country.
 *
 * This function leverages the provided `MainSearchRepository` to fetch teams
 * associated with the specified match type and country.
 *
 * @param repository The repository instance used to query the data for teams.
 * @param matchType The type of match for which teams are to be retrieved.
 * @param country The unique identifier for the country whose teams are to be fetched.
 * @return A list of `Team` objects associated with the given match type and country.
 */
fun getTeamsForCompetitionAndCountry(
    repository: MainSearchRepository,
    matchType: MatchType,
    country: CountryId,
): List<Team> {
    val teams =

        repository.getTeamsForCompetitionAndCountry(
            matchType,
            country
        )


    return teams
}

/**
 * Retrieves the list of matches for a specified tournament.
 *
 * This method uses the provided repository to fetch match details such as
 * participating teams, location, date, and additional details for all matches
 * in a given tournament. The result is encapsulated in an `Either` type to
 * handle success or failure scenarios.
 *
 * @param repository The repository used to fetch tournament match data.
 * @param tournament The name of the tournament for which matches are being fetched.
 *                   Must be a valid tournament name.
 * @return An `Either` containing either an instance of `Error` in case of failure
 *         or a list of `MatchListDto` representing the details of the matches in
 *         the specified tournament.
 */
private fun getMatchesForTournament(
    repository: MainSearchRepository,
    tournament: String
): Either<Error, List<MatchListDto>> {
    return repository.getMatchesForTournament(tournament)
}

/**
 * Finds and retrieves a list of matches based on the specified search parameters.
 *
 * This function queries the provided repository for matches matching the given
 * search criteria encapsulated within a ValidatedMatchSearchParameters object. The
 * result is returned as an Either, containing either an Error or a list of MatchListDto objects.
 *
 * @param repository The repository used for querying match data. It provides methods for accessing
 *                   cricket-related data such as match details, teams, venues, and competitions.
 * @param parameters The validated search parameters used to filter the matches. Includes details like:
 *                   - Home and away teams
 *                   - Date range (start and end dates)
 *                   - Match venue
 *                   - Type of match
 *                   - Match result
 * @return An Either containing:
 *         - An Error, if the operation fails
 *         - A List of MatchListDto, representing the details of matches that match the search parameters
 */
private fun findMatches(repository: MainSearchRepository, parameters: ValidatedMatchSearchParameters): Either<Error, List<MatchListDto>> {
    return repository.findMatches(parameters)
}




