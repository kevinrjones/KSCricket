package com.knowledgespike.feature.teamrecords.domain.usecase

import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.feature.teamrecords.domain.model.*
import com.knowledgespike.feature.teamrecords.domain.repository.TeamRepository

/**
 * A collection of use cases for managing and retrieving team records.
 *
 * @property getTeamOverallTeamRecords Use case for retrieving overall team records.
 * @property getTeamSeriesUseCase Use case for retrieving team records by series.
 * @property getTeamByYearOfMatchStartUseCase Use case for retrieving team records based on the start year of matches.
 * @property getTeamSeasonUseCase Use case for retrieving team records by season.
 * @property getTeamGroundsUseCase Use case for retrieving team records by grounds.
 * @property getTeamByHostCountryUseCase Use case for retrieving team records by host country.
 * @property getTeamByOpponentsUseCase Use case for retrieving team records by opponents.
 * @property getTeamInningsByInningsRecords Use case for retrieving team records by innings.
 * @property getTeamMatchTotalsRecords Use case for retrieving match total records of a team.
 * @property getTeamMatchResultsRecords Use case for retrieving match results of a team.
 * @property getTeamOverallExtras Use case for retrieving overall extras statistics of a team.
 * @property getTeamExtrasByInnings Use case for retrieving extras statistics of a team by innings.
 * @property getTeamHighestTotalChased Use case for retrieving the highest total chased by a team.
 * @property getTeamLowestTargetDefended Use case for retrieving the lowest target defended by a team.
 * @property getTeamLowestTargetDefendedInUnreducedMatch Use case for retrieving the lowest target defended by a team in an unreduced match.
 */
data class TeamRecordsUseCases(
    val getTeamOverallTeamRecords: GetOverallTeamUseCase,
    val getTeamSeriesUseCase: GetTeamSeriesUseCase,
    val getTeamByYearOfMatchStartUseCase: GetTeamByYearOfMatchStartUseCase,
    val getTeamSeasonUseCase: GetTeamSeasonUseCase,
    val getTeamGroundsUseCase: GetTeamGroundsUseCase,
    val getTeamByHostCountryUseCase: GetByHostCountryUseCase,
    val getTeamByOpponentsUseCase: GetByOpponentsUseCase,
    val getTeamInningsByInningsRecords: GetTeamInningsByInningsUseCase,
    val getTeamMatchTotalsRecords: GetTeamMatchTotalsUseCase,
    val getTeamMatchResultsRecords: GetTeamMatchResultsUseCase,
    val getTeamOverallExtras: GetTeamOverallExtras,
    val getTeamExtrasByInnings: GetTeamExtrasByInnings,
    val getTeamHighestTotalChased: GetTeamHighestTotalChased,
    val getTeamLowestTargetDefended: GetTeamLowestTargetDefended,
    val getTeamLowestTargetDefendedInUnreducedMatch: GetTeamLowestTargetDefendedInUnreducedMatch,
)

/**
 * Use case for retrieving the overall summary or details of a team
 * based on the provided search parameters.
 *
 * This use case leverages a repository to query the data source
 * and fetch the relevant team information filtered and validated
 * by the given search criteria.
 *
 * @property repository An instance of the `TeamRepository` used
 * to execute the query for fetching team-related data.
 */
class GetOverallTeamUseCase(val repository: TeamRepository) {
    /**
     * Retrieves the primary team details based on the provided validated search parameters.
     *
     * @param searchParameters The validated search parameters specifying the criteria for the query.
     * @return A [DatabaseResult] containing the team primary records matching the search criteria.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<TeamPrimary> {
        return repository.getByTeamSummary(searchParameters)
    }
}

/**
 * Use case that retrieves team-specific data based on series-related search parameters.
 *
 * @property repository The repository used to query team data from the underlying data source.
 */
class GetTeamSeriesUseCase(val repository: TeamRepository) {
    /**
     * Invokes a search for team information based on the provided validated search parameters.
     *
     * @param searchParameters An instance of ValidatedSearchParameters containing the search criteria,
     * including match type, team identifiers, venue, date range, and other filters.
     * @return A DatabaseResult containing a list of TeamPrimary objects and the count of total items
     * matching the search criteria.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<TeamPrimary> {
        return repository.getTeamBySeries(searchParameters)
    }
}

/**
 * Use case for retrieving a team's details based on the year a match started.
 *
 * This use case provides a specific application layer for fetching team information
 * by leveraging the `getTeamByYearOfMatchStart` functionality of the `TeamRepository`.
 * It facilitates using directly validated search parameters for querying team data
 * according to the year of match start.
 *
 * @property repository The repository interface responsible for fetching team-related data.
 */
class GetTeamByYearOfMatchStartUseCase(val repository: TeamRepository) {
    /**
     * Retrieves a list of teams based on the provided search parameters and returns the results
     * encapsulated in a `DatabaseResult` object.
     *
     * @param searchParameters The validated search parameters containing criteria such as match type,
     * team identifiers, date range, and other details for filtering the results.
     * @return A `DatabaseResult` object containing a list of `TeamPrimary` items that match the provided
     * search criteria, along with the total count of results.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<TeamPrimary> {
        return repository.getTeamByYearOfMatchStart(searchParameters)
    }
}

/**
 * Use case for retrieving information about a team's performance in a specific season.
 *
 * @property repository An instance of `TeamRepository` that provides access to team-related data.
 */
class GetTeamSeasonUseCase(val repository: TeamRepository) {
    /**
     * Invokes the use case to retrieve team information for a specific season based on the given search parameters.
     *
     * @param searchParameters The validated parameters used to filter and query the team data,
     * including information like team ID, opponents, season, date range, venue, and sorting preferences.
     * @return A `DatabaseResult` object containing a list of `TeamPrimary` entries that match the search criteria,
     * along with the total count of matching entries.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<TeamPrimary> {
        return repository.getTeamBySeason(searchParameters)
    }
}

/**
 * Use case for retrieving information about teams based on ground-related search parameters.
 *
 * This use case acts as a wrapper around the `TeamRepository` to fetch data about teams that
 * match the specified ground-related search criteria. It validates the parameters and queries
 * the appropriate method from the repository to obtain the results.
 *
 * @property repository The repository used to interact with the team's data source.
 */
class GetTeamGroundsUseCase(val repository: TeamRepository) {
    /**
     * Invokes the use case to fetch team details based on the given search parameters.
     *
     * @param searchParameters The validated parameters that determine the search criteria, such as match type,
     *                         team identifiers, venue, date range, and other filtering and sorting options.
     * @return A DatabaseResult containing a list of TeamPrimary objects satisfying the search criteria along with
     *         the count of the results.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<TeamPrimary> {
        return repository.getTeamByGrounds(searchParameters)
    }
}

/**
 * UseCase class responsible for retrieving team data based on the host country
 * specified in the validated search parameters.
 *
 * This class directly interacts with the `TeamRepository` to fetch results that
 * match the search criteria provided as input.
 *
 * @param repository The `TeamRepository` from which team data is retrieved.
 */
class GetByHostCountryUseCase(val repository: TeamRepository) {
    /**
     * Retrieves the primary team information based on the provided validated search parameters.
     *
     * @param searchParameters The validated search parameters containing the criteria for filtering
     *        team information, including host country, match type, date range, and other options.
     * @return A `DatabaseResult` containing a list of `TeamPrimary` objects that match the specified
     *         search parameters, along with the total count of matched results.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<TeamPrimary> {
        return repository.getTeamByHostCountry(searchParameters)
    }
}

/**
 * Use case class for retrieving teams based on specific opponent criteria.
 *
 * This use case encapsulates the interaction with a `TeamRepository` to fetch data
 * about teams, filtered by their opponents, utilizing validated search parameters.
 *
 * @property repository The repository instance providing access to team data.
 */
class GetByOpponentsUseCase(val repository: TeamRepository) {
    /**
     * Retrieves team records based on the specified validated search parameters.
     *
     * @param searchParameters The validated search parameters containing criteria like match type,
     *                         opponent team ID, venue, date range, sorting preferences, and more.
     * @return A `DatabaseResult` containing a list of `TeamPrimary` objects matching the search criteria
     *         and the total count of these records.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<TeamPrimary> {
        return repository.getTeamByOpponents(searchParameters)
    }
}

/**
 * Use case for retrieving a team's innings-by-innings performance based on specified search parameters.
 *
 * This class acts as an intermediary between the presentation layer and the `TeamRepository`,
 * abstracting the logic required to query and fetch the relevant data. It leverages the repository's
 * `getTeamInningsByInnings` method to obtain the data while providing a high-level operation for accessing it.
 *
 * @property repository An instance of `TeamRepository` used to perform the underlying data retrieval operations.
 */
class GetTeamInningsByInningsUseCase(val repository: TeamRepository) {
    /**
     * Invokes the retrieval of team innings-by-innings data based on the given search parameters.
     *
     * @param searchParameters The search parameters that have been validated and are used to query the repository.
     * @return A database result containing the team innings-by-innings data.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<TeamInningsByInnings> {
        return repository.getTeamInningsByInnings(searchParameters)
    }
}

/**
 * Use case for retrieving the match totals for a team based on specified search parameters.
 *
 * This class facilitates the retrieval of aggregated match statistics for a team,
 * filtered and customized according to the provided search criteria.
 *
 * @property repository The repository responsible for accessing team-related statistical data.
 */
class GetTeamMatchTotalsUseCase(val repository: TeamRepository) {
    /**
     * Invokes the method to retrieve team match totals based on the provided search parameters.
     *
     * @param searchParameters A ValidatedSearchParameters object containing the criteria for fetching match totals.
     * @return A DatabaseResult containing the TeamInningsByInnings data matching the search criteria.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<TeamInningsByInnings> {
        return repository.getTeamMatchTotals(searchParameters)
    }
}

/**
 * Use case for retrieving match results of a specific team using the provided repository.
 *
 * This class interacts with the `TeamRepository` to fetch team match results
 * based on the search parameters encapsulated in the `ValidatedSearchParameters` object.
 *
 * @property repository The repository instance used to query match results related to the team.
 */
class GetTeamMatchResultsUseCase(val repository: TeamRepository) {
    /**
     * Executes the search operation for team match results based on the provided search parameters.
     *
     * @param searchParameters a validated set of parameters used to perform the search.
     * @return a result containing the team match results based on the search parameters.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<TeamMatchResult> {
        return repository.getTeamMatchResults(searchParameters)
    }
}

/**
 * A use case class for retrieving the overall extras statistics of a team based on provided search parameters.
 *
 * The class serves as a bridge between the application domain logic and the repository layer,
 * delegating the retrieval of data to the `TeamRepository`.
 *
 * @property repository The repository instance used to access the team data.
 */
class GetTeamOverallExtras(val repository: TeamRepository) {
    /**
     * Invokes the retrieval of overall team extras based on the given validated search parameters.
     *
     * @param searchParameters the parameters that have been validated and will be used to query the database for team extras overall information
     * @return a database result containing the overall team extras information
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<TeamExtrasOverall> {
        return repository.getTeamOverallExtras(searchParameters)
    }
}

/**
 * Use case for retrieving the extras statistics for a team, split by innings, based on the provided search parameters.
 *
 * @property repository The repository that provides access to team-related data and statistics.
 */
class GetTeamExtrasByInnings(val repository: TeamRepository) {
    /**
     * Invokes the process of retrieving team extras statistics by innings based on the provided search parameters.
     *
     * @param searchParameters An instance of ValidatedSearchParameters containing the criteria for the search.
     * @return A DatabaseResult containing the data of type TeamExtrasInningsByInnings.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<TeamExtrasInningsByInnings> {
        return repository.getTeamExtrasByInnings(searchParameters)
    }
}

/**
 * A use-case class responsible for retrieving the highest total chased by a team
 * based on provided validated search parameters.
 *
 * @property repository The repository that provides access to team statistical data.
 */
class GetTeamHighestTotalChased(val repository: TeamRepository) {
    /**
     * Invokes the retrieval process for fetching the team target details based on the provided search parameters.
     * Delegates the operation to the repository to get the highest total chased by a team.
     *
     * @param searchParameters a validated object containing criteria for the search operation.
     * @return the result of the operation containing the team target details or an appropriate error status.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<TeamTargetDetailsDto> {
        return repository.getTeamHighestTotalChased(searchParameters)
    }
}

/**
 * Use case for retrieving the lowest target successfully defended by a team based on specific search parameters.
 *
 * @param repository The repository instance used to interact with the underlying data source for team-related statistics.
 */
class GetTeamLowestTargetDefended(val repository: TeamRepository) {
    /**
     * Invokes the function to retrieve the team with the lowest target defended using the provided search parameters.
     *
     * @param searchParameters the validated search parameters used to filter the database query results.
     * @return a database result containing the details of the team with the lowest target defended as a TeamTargetDetailsDto object.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<TeamTargetDetailsDto> {
        return repository.getTeamLowestTargetDefended(searchParameters)
    }
}

/**
 * This class is responsible for retrieving details of the lowest target defended by a team
 * in an unreduced match, utilizing the repository.
 *
 * An unreduced match refers to games not affected by external conditions such as interruptions
 * or adjustments that alter the match duration or format.
 *
 * @constructor Initializes the class with the specified TeamRepository instance to access team data.
 * @param repository The repository used to fetch team-related data.
 */
class GetTeamLowestTargetDefendedInUnreducedMatch(val repository: TeamRepository) {
    /**
     * Invokes the operation to retrieve the team with the lowest target defended in an unreduced match
     * based on the provided search parameters.
     *
     * @param searchParameters the validated search parameters to filter and query the database.
     * @return the result containing details of the team with the lowest target defended.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<TeamTargetDetailsDto> {
        return repository.getTeamLowestTargetDefendedInUnreducedMatch(searchParameters)
    }
}

