package com.knowledgespike.feature.battingrecords.domain.usecase

import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.feature.battingrecords.domain.model.BattingInningsByInnings
import com.knowledgespike.feature.battingrecords.domain.model.BattingPrimary
import com.knowledgespike.feature.battingrecords.domain.repository.BattingRepository
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters

/**
 * A container class that groups various use cases related to retrieving batting records.
 *
 * @property getIndividualOverallBattingRecords Use case for fetching overall batting records for an individual.
 * @property getIndividualSeriesUseCase Use case for fetching batting records by series for an individual.
 * @property getIndividualByYearOfMatchStartUseCase Use case for fetching batting records by the year a match started for an individual.
 * @property getIndividualSeasonUseCase Use case for fetching batting records by season for an individual.
 * @property getIndividualGroundsUseCase Use case for fetching batting records by grounds for an individual.
 * @property getByHostCountryUseCase Use case for fetching batting records by host country.
 * @property getByOpponentsUseCase Use case for fetching batting records against specific opponents.
 * @property getIndividualInningsByInningsRecords Use case for fetching detailed innings-by-innings records for an individual.
 * @property gatIndividualMatchTotalsRecords Use case for fetching match total records for an individual.
 */
data class BattingRecordsUseCases(
    val getIndividualOverallBattingRecords: GetIndividualOverallBattingUseCase,
    val getIndividualSeriesUseCase: GetIndividualSeriesUseCase,
    val getIndividualByYearOfMatchStartUseCase: GetIndividualByYearOfMatchStartUseCase,
    val getIndividualSeasonUseCase: GetIndividualSeasonUseCase,
    val getIndividualGroundsUseCase: GetIndividualGroundsUseCase,
    val getByHostCountryUseCase: GetByHostCountryUseCase,
    val getByOpponentsUseCase: GetByOpponentsUseCase,
    val getIndividualInningsByInningsRecords: GetIndividualInningsByInningsUseCase,
    val gatIndividualMatchTotalsRecords: GetIndividualMatchTotalsUseCase,
)

/**
 * Use case for retrieving overall batting statistics for an individual.
 *
 * This class provides functionality to query and retrieve overall batting statistics
 * based on validated search parameters through the repository interface.
 *
 * @param repository Instance of `BattingRepository` to interact with the data layer.
 */
class GetIndividualOverallBattingUseCase(val repository: BattingRepository) {
    /**
     * Invokes the use case to fetch individual overall batting statistics based on the provided search parameters.
     *
     * @param searchParameters The validated parameters used to filter and query the batting statistics.
     * @return A database result containing a list of batting statistics and the total count of matches.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<BattingPrimary> {
        return repository.getByIndividualOverallBatting(searchParameters)
    }
}

/**
 * Use case for retrieving individual batting series statistics.
 *
 * This class serves as an intermediary between the presentation and data layers,
 * facilitating the retrieval of batting statistics for individual player series
 * based on the provided search criteria. It invokes the `getByIndividualSeries`
 * method of the `BattingRepository` to obtain the data.
 *
 * @constructor Initializes the use case with the specified repository.
 * @property repository The repository through which the data is retrieved.
 */
class GetIndividualSeriesUseCase(val repository: BattingRepository) {
    /**
     * Invokes the use case to retrieve batting primary statistics based on the provided search parameters.
     *
     * This operator function queries the repository to fetch data that matches the given validated search parameters.
     *
     * @param searchParameters A set of validated parameters used to filter and query the batting statistics.
     *                         This includes match type, team IDs, venue, date range, sorting preferences,
     *                         and other criteria.
     * @return A result containing a list of batting primary data and a total count of the records retrieved.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<BattingPrimary> {
        return repository.getByIndividualSeries(searchParameters)
    }
}

/**
 * Use case for retrieving batting statistics by the year of match start.
 *
 * This class serves as the operator for invoking the specific repository function
 * that queries match records filtered by the year in which the match started.
 *
 * @property repository The repository providing access to batting statistics data.
 */
class GetIndividualByYearOfMatchStartUseCase(val repository: BattingRepository) {
    /**
     * Invokes the use case to retrieve batting primary records based on the given validated search parameters.
     *
     * @param searchParameters The validated search parameters containing the criteria for retrieving batting records.
     * @return A DatabaseResult containing a list of BattingPrimary records and the total count of records matching the criteria.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<BattingPrimary> {
        return repository.getByIndividualYearOfMatchStart(searchParameters)
    }
}

/**
 * Use case for retrieving batting statistics for an individual player during a specific season.
 *
 * This class acts as an intermediary between the presentation and data layers,
 * coordinating the process of querying the repository to fetch season-specific
 * batting data based on validated search parameters.
 *
 * @constructor Initializes the use case with the given batting repository.
 * @property repository The repository responsible for fetching batting data.
 */
class GetIndividualSeasonUseCase(val repository: BattingRepository) {
    /**
     * Executes a query to retrieve batting statistics for an individual season based on the provided search parameters.
     *
     * @param searchParameters The validated search parameters containing filters and configurations
     *                         for the statistics query, such as match type, teams, season, and more.
     * @return A `DatabaseResult` containing a list of `BattingPrimary` objects matching the query,
     *         along with the total count of retrieved items.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<BattingPrimary> {
        return repository.getByIndividualSeason(searchParameters)
    }
}

/**
 * Use case for retrieving batting statistics for individual players based on grounds.
 *
 * This class acts as an intermediary between the application and the repository
 * to fetch data filtered by specific grounds, utilizing validated search parameters.
 * It encapsulates the logic for querying the repository and obtaining the results
 * as a `DatabaseResult` containing batting data.
 *
 * @constructor Initializes the use case with the specified `BattingRepository`.
 * @property repository The repository used to fetch batting statistics.
 */
class GetIndividualGroundsUseCase(val repository: BattingRepository) {
    /**
     * Invokes the use case to retrieve batting primary data for individual grounds based on the
     * specified validated search parameters.
     *
     * @param searchParameters The validated search parameters containing criteria like match type,
     * team IDs, ground ID, date range, sorting preferences, and other filters for querying.
     * @return A [DatabaseResult] containing a list of batting primary data and the total count of records.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<BattingPrimary> {
        return repository.getByIndividualGrounds(searchParameters)
    }
}

/**
 * Use case class for retrieving batting statistics based on the host country.
 *
 * This use case leverages the provided repository to fetch batting records
 * that align with the specified search parameters, which include the host
 * country as a filtering criterion.
 *
 * @constructor Creates a new instance of the use case with the given repository.
 * @param repository The repository used to fetch batting statistics.
 */
class GetByHostCountryUseCase(val repository: BattingRepository) {
    /**
     * Invokes the repository to retrieve batting primary data based on validated search parameters.
     *
     * @param searchParameters The validated search parameters used to filter the batting primary data.
     * @return A DatabaseResult containing the retrieved batting primary data matching the search parameters.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<BattingPrimary> {
        return repository.getByHostCountry(searchParameters)
    }
}

/**
 * Use case class for retrieving batting statistics based on opponents.
 *
 * This use case integrates with a `BattingRepository` to fetch batting data
 * filtered by the opposing team and other provided parameters. It expects
 * validated search parameters in the form of a `ValidatedSearchParameters` object
 * and returns the result as a `DatabaseResult` containing a list of `BattingPrimary` records.
 *
 * @constructor Creates an instance of the `GetByOpponentsUseCase` with the provided repository.
 * @param repository The repository used to query batting statistics.
 */
class GetByOpponentsUseCase(val repository: BattingRepository) {
    /**
     * Invokes a search operation based on the provided validated search parameters and retrieves the result from the repository.
     *
     * @param searchParameters The validated search parameters used to filter and query the data.
     * @return A `DatabaseResult` containing the filtered list of batting primary data.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<BattingPrimary> {
        return repository.getByOpponents(searchParameters)
    }
}

/**
 * Use case class for fetching individual batting innings records.
 *
 * This class is responsible for retrieving batting data for an individual player
 * across different innings based on the provided search parameters. It acts as a
 * layer to coordinate data access between the application's business logic and the
 * underlying data layer.
 *
 * @property repository The repository used to access batting data.
 */
class GetIndividualInningsByInningsUseCase(val repository: BattingRepository) {
    /**
     * Invokes the repository to fetch batting innings by innings data based on the provided search parameters.
     *
     * @param searchParameters the validated search parameters used for querying the database
     * @return a `DatabaseResult` containing the batting innings by innings data matching the search parameters
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<BattingInningsByInnings> {
        return repository.getByIndividualInningsByInnings(searchParameters)
    }
}

/**
 * Use case class for retrieving individual match totals.
 *
 * This class acts as an intermediary layer between the repository and the upper-level application
 * layer to fetch batting data for individual match totals based on the provided search parameters.
 * It utilizes the repository to perform the underlying data retrieval operation.
 *
 * @property repository The repository instance used for accessing batting data.
 */
class GetIndividualMatchTotalsUseCase(val repository: BattingRepository) {
    /**
     * Invokes the function to retrieve batting innings data by match totals based on the provided search parameters.
     *
     * @param searchParameters the validated search parameters used to filter the data query
     * @return the result of the database query containing the batting innings by innings data
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<BattingInningsByInnings> {
        return repository.getByIndividualMatchTotals(searchParameters)
    }
}

