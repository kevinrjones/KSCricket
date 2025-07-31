package com.knowledgespike.feature.fieldingrecords.domain.usecase

import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.feature.fieldingrecords.domain.model.FieldingInningsByInnings
import com.knowledgespike.feature.fieldingrecords.domain.model.FieldingPrimary
import com.knowledgespike.feature.fieldingrecords.domain.repository.FieldingRepository
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters

/**
 * Container class for all use cases related to retrieving fielding records.
 *
 * @property getIndividualOverallFieldingRecords Provides functionality to retrieve overall fielding records for an individual.
 * @property getIndividualSeriesUseCase Allows fetching fielding records based on series for an individual.
 * @property getIndividualByYearOfMatchStartUseCase Enables retrieval of fielding records for an individual based on the year matches started.
 * @property getIndividualSeasonUseCase Supports fetching fielding records for an individual based on specific seasons.
 * @property getIndividualGroundsUseCase Provides fielding records for an individual filtered by grounds where matches were played.
 * @property getByHostCountryUseCase Retrieves fielding records for an individual filtered by the host country where matches occurred.
 * @property getByOpponentsUseCase Allows fetching fielding records for an individual filtered by opponents.
 * @property getIndividualInningsByInningsRecords Provides functionality to fetch fielding performance for an individual on an innings-by-innings basis.
 * @property gatIndividualMatchTotalsRecords Retrieves aggregated match totals for an individual’s fielding performance.
 */
data class FieldingRecordsUseCases(
    val getIndividualOverallFieldingRecords: GetIndividualOverallFieldingUseCase,
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
 * Use case for retrieving overall fielding statistics for an individual player.
 *
 * This class interacts with the `FieldingRepository` to fetch fielding statistics based on the
 * provided search parameters, such as match type, date range, team, opponents, and more.
 *
 * @property repository The repository used to query the fielding data.
 */
class GetIndividualOverallFieldingUseCase(val repository: FieldingRepository) {
    /**
     * Invokes the use case to retrieve overall fielding statistics for an individual based on the specified search parameters.
     *
     * @param searchParameters The validated parameters that define the filter criteria for the query. These parameters
     * include details such as match type, teams, opponents, ground, host country, date range, sorting preferences, and paging configuration.
     * @return A `DatabaseResult` containing a list of `FieldingPrimary` objects that match the specified search parameters,
     * along with the total count of matching records.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<FieldingPrimary> {
        return repository.getByIndividualOverallFielding(searchParameters)
    }
}

/**
 * Use case for retrieving fielding statistics for an individual across a series of matches.
 *
 * This class is primarily responsible for invoking the appropriate repository method
 * to fetch fielding data based on validated search parameters.
 *
 * @property repository The repository used to access fielding data.
 */
class GetIndividualSeriesUseCase(val repository: FieldingRepository) {
    /**
     * Invokes the use case to retrieve fielding statistics for individual players based on the provided search parameters.
     *
     * @param searchParameters An instance of ValidatedSearchParameters containing filters such as match type, teams, dates,
     *                         venues, sorting preferences, and paging options to refine the query.
     * @return A DatabaseResult containing a list of FieldingPrimary objects representing the retrieved fielding statistics
     *         and the total count of the results.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<FieldingPrimary> {
        return repository.getByIndividualSeries(searchParameters)
    }
}

/**
 * Use case for retrieving fielding statistics grouped by the year of match start.
 *
 * @property repository The FieldingRepository instance used to query the data.
 */
class GetIndividualByYearOfMatchStartUseCase(val repository: FieldingRepository) {
    /**
     * Invokes the use case to retrieve fielding statistics grouped by the year of match start.
     *
     * @param searchParameters The validated search parameters containing criteria such as match type,
     * team, opponents, venue, date range, and sorting preferences for querying fielding statistics.
     * @return A `DatabaseResult` containing a list of `FieldingPrimary` records that match the search
     * criteria and the total count of such records.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<FieldingPrimary> {
        return repository.getByIndividualYearOfMatchStart(searchParameters)
    }
}

/**
 * Use case for retrieving fielding statistics for an individual player based on a specific season.
 *
 * This class interacts with the `FieldingRepository` to fetch data filtered by season, using the
 * criteria provided in the `ValidatedSearchParameters`.
 *
 * @property repository The repository from which the data is fetched.
 */
class GetIndividualSeasonUseCase(val repository: FieldingRepository) {
    /**
     * Invokes the search for fielding statistics based on individual season parameters.
     *
     * @param searchParameters The validated search parameters containing criteria such as match type, team, opponents, venue, date range, season, and sorting preferences.
     * @return A `DatabaseResult` containing a list of `FieldingPrimary` objects with fielding statistics and the total count of records retrieved.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<FieldingPrimary> {
        return repository.getByIndividualSeason(searchParameters)
    }
}

/**
 * Use case responsible for retrieving fielding statistics for individuals based on specific grounds.
 *
 * This class acts as an intermediary between the domain layer and the data layer, using the
 * FieldingRepository to execute queries with validated search parameters. It enables fetching
 * fielding data filtered by grounds, providing results in the form of a DatabaseResult containing
 * fielding records and counts.
 *
 * @property repository The repository interface for accessing fielding statistics data.
 */
class GetIndividualGroundsUseCase(val repository: FieldingRepository) {
    /**
     * Retrieves fielding records for an individual player based on specific search parameters.
     *
     * Delegates the search to the repository, applying filters such as specific grounds, teams,
     * match types, and other criteria provided in the `ValidatedSearchParameters`.
     *
     * @param searchParameters The validated search criteria, including match type, grounds, date range,
     * team details, sorting preferences, and more.
     * @return A `DatabaseResult` containing a list of `FieldingPrimary` items and the total count of matching records.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<FieldingPrimary> {
        return repository.getByIndividualGrounds(searchParameters)
    }
}

/**
 * Use case for retrieving fielding statistics based on the host country.
 *
 * This use case facilitates the execution of a fielding data query
 * by leveraging the `FieldingRepository` to fetch results filtered
 * specifically by the host country. It utilizes validated search
 * parameters to ensure accurate and relevant data retrieval.
 *
 * @property repository The repository instance used to execute the fielding statistics query.
 */
class GetByHostCountryUseCase(val repository: FieldingRepository) {
    /**
     * Invokes the use case to retrieve fielding statistics based on the provided validated search parameters.
     *
     * @param searchParameters Validated search parameters containing criteria such as match type, opponents,
     * host country, date range, sorting preferences, and paging configurations.
     * @return A `DatabaseResult` object containing a list of `FieldingPrimary` records that match the search
     * criteria along with the total count of such records.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<FieldingPrimary> {
        return repository.getByHostCountry(searchParameters)
    }
}

/**
 * Use case for retrieving fielding statistics associated with specific opponents.
 *
 * This class provides a method to invoke a query for fetching fielding data filtered by opposing teams,
 * leveraging a repository implementation of `FieldingRepository`.
 *
 * @property repository The repository used for accessing fielding statistics data.
 */
class GetByOpponentsUseCase(val repository: FieldingRepository) {
    /**
     * Invokes the use case to retrieve fielding statistics based on the provided search parameters.
     *
     * @param searchParameters The validated search parameters used to filter the fielding statistics.
     * These parameters include details such as match type, opposing teams, ground information, date range,
     * and sorting preferences.
     * @return A `DatabaseResult` containing a list of `FieldingPrimary` objects that match the search criteria,
     * along with the count of total matching records.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<FieldingPrimary> {
        return repository.getByOpponents(searchParameters)
    }
}

/**
 * Use case for retrieving detailed fielding statistics for an individual player,
 * organized on an innings-by-innings basis.
 *
 * This class interacts with the `FieldingRepository` to fetch the relevant
 * fielding data based on the provided search parameters.
 *
 * @constructor Creates an instance using the given fielding repository.
 *
 * @param repository The repository responsible for handling fielding-related database queries.
 */
class GetIndividualInningsByInningsUseCase(val repository: FieldingRepository) {
    /**
     * Retrieves fielding innings-by-innings statistics for an individual player based on the provided search parameters.
     *
     * This operator function acts as an entry point to fetch the data by delegating the query to the underlying repository.
     *
     * @param searchParameters The validated search parameters containing filtering and sorting criteria,
     * including match type, teams, opponents, venue, date range, sorting preferences, and pagination options.
     * @return A `DatabaseResult` containing a list of `FieldingInningsByInnings` objects and the total count of the results.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<FieldingInningsByInnings> {
        return repository.getByIndividualInningsByInnings(searchParameters)
    }
}

/**
 * Use case for retrieving fielding statistics for an individual player grouped by match totals.
 * This class interacts with the `FieldingRepository` interface to query the required data
 * based on the provided search parameters.
 *
 * @property repository The repository used to fetch fielding statistics data.
 */
class GetIndividualMatchTotalsUseCase(val repository: FieldingRepository) {
    /**
     * Invokes the repository function to retrieve fielding statistics for an individual player,
     * grouped by match totals, based on the given validated search parameters.
     *
     * @param searchParameters The validated search parameters that define the criteria for the query,
     *                         including match type, team identifiers, venue, date range, and other filters.
     * @return A `DatabaseResult` containing a list of `FieldingInningsByInnings` objects that match
     *         the criteria specified in the search parameters, and the total count of records.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<FieldingInningsByInnings> {
        return repository.getByIndividualMatchTotals(searchParameters)
    }
}

